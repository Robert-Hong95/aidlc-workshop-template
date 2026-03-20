FROM node:20-alpine AS base
RUN corepack enable

FROM base AS builder
WORKDIR /app
COPY . .
RUN pnpm install --frozen-lockfile
RUN pnpm --filter @table-order/customer build

FROM base AS runner
WORKDIR /app
ENV NODE_ENV=production
COPY --from=builder /app/apps/customer/.next/standalone ./
COPY --from=builder /app/apps/customer/.next/static ./apps/customer/.next/static
COPY --from=builder /app/apps/customer/public ./apps/customer/public
EXPOSE 3000
CMD ["node", "apps/customer/server.js"]
