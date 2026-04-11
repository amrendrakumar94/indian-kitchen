local key = KEYS[1]
local capacity = tonumber(ARGV[1])
local refill_rate = tonumber(ARGV[2])
local now_ms = tonumber(ARGV[3])
local requested = tonumber(ARGV[4])

local state = redis.call('HMGET', key, 'tokens', 'last_refill_ms')
local tokens = tonumber(state[1])
local last_refill_ms = tonumber(state[2])

if tokens == nil then
  tokens = capacity
end

if last_refill_ms == nil then
  last_refill_ms = now_ms
end

local elapsed_ms = math.max(0, now_ms - last_refill_ms)
local refill_tokens = (elapsed_ms / 1000.0) * refill_rate
tokens = math.min(capacity, tokens + refill_tokens)

local allowed = 0
local retry_after_ms = 0

if tokens >= requested then
  tokens = tokens - requested
  allowed = 1
else
  retry_after_ms = math.ceil(((requested - tokens) / refill_rate) * 1000)
end

redis.call('HMSET', key, 'tokens', tokens, 'last_refill_ms', now_ms)

local ttl_ms = math.ceil((capacity / refill_rate) * 2000)
if ttl_ms < 1000 then
  ttl_ms = 1000
end
redis.call('PEXPIRE', key, ttl_ms)

return {allowed, math.floor(tokens), retry_after_ms}
