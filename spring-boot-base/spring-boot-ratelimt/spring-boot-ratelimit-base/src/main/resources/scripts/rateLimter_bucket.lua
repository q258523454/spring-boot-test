-- key
local key = KEYS[1];
-- 桶大小(limit)
local limit = tonumber(ARGV[1]);
-- 间隔步长——每次间隔向桶中添加的令牌数量
local stepValue = tonumber(ARGV[2]);
-- 间隔时间——向令牌桶中添加数据的时间间隔,以秒为单位
local stepTime = tonumber(ARGV[3]);
-- 当前时间
local nowTime = tonumber(ARGV[4]);
-- 过期时间,多加1秒
local expireTimePlusOne = (math.floor(limit / stepValue) * stepTime + 1000) / 1000;
-- 转换成整数
local expireTime = math.ceil(expireTimePlusOne)

local lastClearTimeKey = 'redis-bucket-lastClearTime-'..key
local lastClearTime = redis.call('GET', lastClearTimeKey);
local hasKey = redis.call('EXISTS', key);

if hasKey == 1 and lastClearTime then
    -- 时间间隔
    local diffTime = tonumber(nowTime) - tonumber(lastClearTime);
    -- 当前值
    local count = tonumber(redis.call('GET', key));
    if  diffTime >= stepTime then
            local maxValue = count + math.floor(diffTime / stepTime) * stepValue;
            if maxValue > tonumber(limit) then
                count = limit;
            else
                count = maxValue;
            end
            redis.call('SETEX', lastClearTimeKey, expireTime, nowTime);
            redis.call('SETEX', key, expireTime, count);
    end

    -- 无可用令牌
    if count <= 0 then
        return -1;
    else
        -- 计数器减1
        redis.call('DECR', key);
    end
else
    -- 首次创建桶
    redis.call('SETEX', key, expireTime, math.floor(stepValue));
    redis.call('SETEX', lastClearTimeKey, expireTime, nowTime);
end

return 1;
