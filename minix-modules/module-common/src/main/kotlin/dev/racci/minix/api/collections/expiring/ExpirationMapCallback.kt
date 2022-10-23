package dev.racci.minix.api.collections.expiring

public fun interface ExpirationMapCallback<K, V> {
    public fun onExpire(key: K, value: V)
}
