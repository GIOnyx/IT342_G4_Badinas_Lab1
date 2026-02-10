<#
start-backend.ps1
Loads .env (if present) into the process environment and starts the backend.
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

if (-not $env:SPOONACULAR_API_KEY) {
    Write-Warning 'SPOONACULAR_API_KEY not set. Please add it to .env or your environment.'
}

Push-Location (Join-Path $repoRoot 'backend')
try {
    if (Test-Path '.\mvnw.cmd') {
        Write-Output 'Starting backend with mvnw.cmd spring-boot:run'
        & .\mvnw.cmd spring-boot:run
    } else {
        Write-Output 'Starting backend with mvn spring-boot:run'
        & mvn spring-boot:run
    }
} finally {
    Pop-Location
}
