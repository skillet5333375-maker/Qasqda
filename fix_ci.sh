# File: fix_ci.sh
set -euo pipefail

ROOT_DIR="$(pwd)"

require_file() {
  if [ ! -f "$1" ]; then
    echo "ERROR: missing file: $1" >&2
    exit 1
  fi
}

echo "==> Checking project layout..."
require_file "$ROOT_DIR/settings.gradle.kts"
require_file "$ROOT_DIR/build.gradle.kts"
require_file "$ROOT_DIR/gradlew"
require_file "$ROOT_DIR/app/build.gradle.kts"

echo "==> 1) Fix root build.gradle.kts (plugins only)..."
cat > "$ROOT_DIR/build.gradle.kts" <<'EOF'
// File: build.gradle.kts (ROOT)
plugins {
    id("com.android.application") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.20" apply false
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}
EOF

echo "==> 2) Ensure gradle.properties (AndroidX + Jetifier)..."
GP="$ROOT_DIR/gradle.properties"
touch "$GP"
grep -q '^android\.useAndroidX=' "$GP" 2>/dev/null || echo 'android.useAndroidX=true' >> "$GP"
grep -q '^android\.enableJetifier=' "$GP" 2>/dev/null || echo 'android.enableJetifier=true' >> "$GP"
grep -q '^org\.gradle\.jvmargs=' "$GP" 2>/dev/null || echo 'org.gradle.jvmargs=-Xmx2g -Dfile.encoding=UTF-8' >> "$GP"
grep -q '^kotlin\.code\.style=' "$GP" 2>/dev/null || echo 'kotlin.code.style=official' >> "$GP"

echo "==> 3) Patch app/build.gradle.kts (exclude com.android.support + legacy-support-v4)..."
APP_BG="$ROOT_DIR/app/build.gradle.kts"

# Add legacy-support-v4 if not present
if ! grep -q 'androidx\.legacy:legacy-support-v4' "$APP_BG"; then
  # Insert into dependencies block if exists
  if grep -q 'dependencies\s*{' "$APP_BG"; then
    awk '
      BEGIN{done=0}
      {
        print $0
        if (!done && $0 ~ /dependencies\s*\{/){
          print "    // AndroidX replacement for old support-v4 (helps legacy libs)"
          print "    implementation(\"androidx.legacy:legacy-support-v4:1.0.0\")"
          done=1
        }
      }
    ' "$APP_BG" > "$APP_BG.tmp" && mv "$APP_BG.tmp" "$APP_BG"
  else
    # If no dependencies block, append one (rare)
    cat >> "$APP_BG" <<'EOF'

dependencies {
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
}
EOF
  fi
fi

# Add exclude block if not present
if ! grep -q 'exclude\(group = "com\.android\.support"\)' "$APP_BG"; then
  cat >> "$APP_BG" <<'EOF'

/**
 * Drop old com.android.support:* artifacts. They frequently break manifest merging in AndroidX projects.
 */
configurations.configureEach {
    exclude(group = "com.android.support")
}
EOF
fi

echo "==> 4) Add valid GitHub Actions workflow..."
mkdir -p "$ROOT_DIR/.github/workflows"
cat > "$ROOT_DIR/.github/workflows/android-debug-apk.yml" <<'EOF'
name: Build Android Debug APK

on:
  workflow_dispatch:
  push:
    branches: ["main", "master"]
  pull_request:

jobs:
  build-debug:
    runs-on: ubuntu-latest
    permissions:
      contents: read

    steps:
      - name: Checkout
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: "17"
          cache: gradle

      - name: Grant execute permission for gradlew
        run: chmod +x ./gradlew

      - name: Dependency insight (who pulls com.android.support)
        run: ./gradlew :app:dependencyInsight --dependency com.android.support --configuration debugRuntimeClasspath --no-daemon

      - name: Build Debug APK (capture log)
        shell: bash
        run: |
          set -o pipefail
          ./gradlew :app:assembleDebug \
            --no-daemon --stacktrace --info --warning-mode all --console=plain \
            2>&1 | tee gradle-build.log

      - name: Extract failure summary (always)
        if: always()
        shell: bash
        run: |
          if [ -f gradle-build.log ]; then
            echo "==== FAILURE SNIPPET (if any) ===="
            grep -nE "FAILURE:|\\* What went wrong:|Execution failed for task|Caused by:" gradle-build.log | head -n 200 || true
            echo "==== LAST 200 LINES ===="
            tail -n 200 gradle-build.log || true
          else
            echo "gradle-build.log not found (build likely failed before tee)."
          fi

      - name: Upload logs and reports (always)
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: build-logs-and-reports
          path: |
            gradle-build.log
            build/reports/problems/**/*
            app/build/reports/**/*
            app/build/outputs/logs/manifest-merger-*.txt
            app/build/intermediates/merged_manifest/**/*
          if-no-files-found: warn

      - name: Upload Debug APK
        if: success()
        uses: actions/upload-artifact@v4
        with:
          name: app-debug-apk
          path: app/build/outputs/apk/debug/*.apk
          if-no-files-found: error
EOF

echo "==> 5) Optional: remove broken blank.yml if exists..."
if [ -f "$ROOT_DIR/.github/workflows/blank.yml" ]; then
  rm -f "$ROOT_DIR/.github/workflows/blank.yml"
fi

echo "==> 6) Done."
echo
echo "Next:"
echo "  git status"
echo "  git add ."
echo "  git commit -m \"Fix CI + AndroidX/manifest merge\""
echo "  git push"
