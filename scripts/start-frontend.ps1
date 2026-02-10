<#
start-frontend.ps1
Loads .env (if present) into the process environment and starts the frontend dev server.
#>
Set-StrictMode -Version Latest
$ScriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Definition

function Load-EnvFile($path) {
    if (-not (Test-Path $path)) { return }
    Get-Content $path | ForEach-Object {
        if ($_ -and ($_ -notmatch '^[\s#]')) {
            $parts = $_ -split '=',2
            if ($parts.Count -eq 2) {
                $k = $parts[0].Trim()
                $v = $parts[1].Trim().Trim('"')
                Set-Item -Path Env:\$k -Value $v
            }
        }
    }
}

# Load .env from repo root
$repoRoot = Join-Path $ScriptRoot '..' | Resolve-Path -ErrorAction SilentlyContinue
if ($repoRoot) { $envFile = Join-Path $repoRoot ' .env'.Trim() } else { $envFile = Join-Path $ScriptRoot '..\.env' }
if (Test-Path $envFile) {
    Write-Output "Loading environment from $envFile"
    Load-EnvFile $envFile
}

Push-Location (Join-Path $repoRoot 'web')
try {
    if (Test-Path 'package.json') {
        Write-Output 'Starting frontend with npm run dev'
        & npm run dev
    } else {
        Write-Warning 'Frontend package.json not found in web folder.'
        Read-Host -Prompt 'Press Enter to exit'
    }
} finally {
    Pop-Location
}
