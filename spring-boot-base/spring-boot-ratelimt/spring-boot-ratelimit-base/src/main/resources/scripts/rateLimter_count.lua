-- 获取参数
local key = KEYS[1];
local expire = ARGV[1]
local limit = ARGV[2]

local hasKey = redis.call('EXISTS',key);

-- 计算limit数, 超过限流则返回 -1
if hasKey == 1 then
    local value = tonumber(redis.call('GET',key));
    if value >= tonumber(limit) then
        return -1;
    end
end
redis.call('INCR',key);

local ttl = redis.call('TTL',key);

-- 首次创建设置过期时间
if ttl < 0 then
    redis.call('EXPIRE',key,expire);
end

return 1;
