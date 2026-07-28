#!/usr/bin/env bash
set -euo pipefail

# Run on a fresh Ubuntu 22.04/24.04 server as root.
# Usage:
#   bash deploy-ubuntu.sh api.example.com

API_DOMAIN="${1:-}"
if [[ -z "$API_DOMAIN" ]]; then
  echo "Usage: bash deploy-ubuntu.sh api.example.com"
  exit 1
fi

apt-get update
apt-get install -y openjdk-17-jre-headless mysql-server nginx certbot python3-certbot-nginx

id huaxin >/dev/null 2>&1 || useradd --system --home /opt/huaxin --shell /usr/sbin/nologin huaxin
mkdir -p /opt/huaxin/upload /etc/huaxin
chown -R huaxin:huaxin /opt/huaxin
chmod 750 /opt/huaxin

echo "Base packages installed."
echo "Next steps:"
echo "1. Upload server.jar to /opt/huaxin/server.jar"
echo "2. Copy env.example to /etc/huaxin/huaxin-server.env and edit secrets"
echo "3. Import schema.sql into MySQL"
echo "4. Install huaxin-server.service"
echo "5. Configure Nginx for ${API_DOMAIN} and run certbot --nginx -d ${API_DOMAIN}"
