import type { NextConfig } from 'next';

const nextConfig: NextConfig = {
  transpilePackages: ['@table-order/ui', '@table-order/api-client', '@table-order/shared'],
};

export default nextConfig;
