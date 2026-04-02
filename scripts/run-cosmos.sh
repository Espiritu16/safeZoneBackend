#!/usr/bin/env bash
set -euo pipefail

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
ENV_FILE="$PROJECT_DIR/.env"

if [[ ! -f "$ENV_FILE" ]]; then
  cat <<'EOF'
ERROR: No existe el archivo .env en la raiz del proyecto.
Pasos:
1) cp .env.example .env
2) Completa AZURE_COSMOS_ENDPOINT, AZURE_COSMOS_KEY y AZURE_COSMOS_DATABASE
3) Ejecuta de nuevo: ./scripts/run-cosmos.sh
EOF
  exit 1
fi

set -a
# shellcheck disable=SC1090
source "$ENV_FILE"
set +a

required_vars=("AZURE_COSMOS_ENDPOINT" "AZURE_COSMOS_KEY" "AZURE_COSMOS_DATABASE")
for var_name in "${required_vars[@]}"; do
  if [[ -z "${!var_name:-}" ]]; then
    echo "ERROR: La variable $var_name esta vacia en .env"
    exit 1
  fi
done

if [[ ! "$AZURE_COSMOS_ENDPOINT" =~ ^https?:// ]]; then
  echo "ERROR: AZURE_COSMOS_ENDPOINT no parece una URL valida."
  exit 1
fi

cd "$PROJECT_DIR"
mvn spring-boot:run -Dspring-boot.run.profiles=cosmos
