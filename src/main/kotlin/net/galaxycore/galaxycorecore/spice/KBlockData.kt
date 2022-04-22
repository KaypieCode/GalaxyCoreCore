package net.galaxycore.galaxycorecore.spice

import org.bukkit.Chunk
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin
import java.util.*
import java.util.function.Consumer
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.stream.Collectors


class KBlockData (block: Block, plugin: Plugin) : PersistentDataContainer {
    private val keyRegex: Pattern = Pattern.compile("^x(\\d+)y(-?\\d+)z(\\d+)$")
    private val chunkMinXZ = 0
    private val chunkMaxXZ = 15
    private var pdc: PersistentDataContainer? = null
    private var chunk: Chunk? = null
    private var key: NamespacedKey? = null

    init {
        chunk = block.chunk
        key = NamespacedKey(plugin, getOldKey(block))
        pdc = getPersistentDataContainer()
    }

    /**
     * Returns a Set&lt;Block&gt; of all blocks in this chunk containing Custom Block Data created by the given plugin
     *
     * @param plugin Plugin
     * @param chunk Chunk
     *
     * @return A Set containing all blocks in this chunk containing Custom Block Data created by the given plugin
     */
    fun getBlocksWithCustomData(plugin: Plugin, chunk: Chunk): Set<Block?>? {
        val dummy = NamespacedKey(plugin, "dummy")
        return getBlocksWithCustomData(chunk, dummy)
    }

    private fun getBlocksWithCustomData(chunk: Chunk, dummy: NamespacedKey): Set<Block?>? {
        val chunkPDC = chunk.persistentDataContainer
        return chunkPDC.keys.stream()
            .filter { it.namespace == dummy.namespace }
            .map{ getBlockFromKey(it, chunk )}
            .filter(Objects::nonNull)
            .collect(Collectors.toSet())
    }

    private fun getBlockFromKey(key: NamespacedKey, chunk: Chunk): Block? {
        val matcher: Matcher = keyRegex.matcher(key.key)
        if (!matcher.matches()) return null
        val x: Int = matcher.group(1).toInt()
        val y: Int = matcher.group(2).toInt()
        val z: Int = matcher.group(3).toInt()
        return if (x < chunkMinXZ || x > chunkMaxXZ || z < chunkMinXZ || z > chunkMaxXZ || y < getWorldMinHeight(chunk.world) || y > chunk.world.maxHeight - 1
        ) null else chunk.getBlock(x, y, z)
    }

    private fun getWorldMinHeight(world: World): Int {
        calcHas()
        return if (hasWorldInfoGetMinHeightMethod) {
            world.minHeight
        } else {
            0
        }
    }

    /**
     * Gets a String-based NamespacedKey that consists of the block's relative coordinates within its chunk
     *
     * @param block block
     * @return NamespacedKey consisting of the block's relative coordinates within its chunk
     */
    private fun getOldKey(block: Block): String {
        val x: Int = block.x and 0x000F
        val y: Int = block.y
        val z: Int = block.z and 0x000F
        return String.format("x%dy%dz%d", x, y, z)
    }

    /**
     * Removes all custom block data
     */
    fun clear() {
        pdc!!.keys.forEach(Consumer { key: NamespacedKey? -> pdc!!.remove(key!!) })
        save()
    }

    /**
     * Gets the PersistentDataContainer associated with this block.
     *
     * @return PersistentDataContainer of this block
     */
    private fun getPersistentDataContainer(): PersistentDataContainer? {
        val chunkPDC = chunk!!.persistentDataContainer
        val blockPDC: PersistentDataContainer?
        if (chunkPDC.has(key!!, PersistentDataType.TAG_CONTAINER)) {
            blockPDC = chunkPDC.get(key!!, PersistentDataType.TAG_CONTAINER)
            assert(blockPDC != null)
            return blockPDC
        }
        blockPDC = chunkPDC.adapterContext.newPersistentDataContainer()
        //chunkPDC.set(key, PersistentDataType.TAG_CONTAINER, blockPDC);
        return blockPDC
    }

    /**
     * Saves the block's PersistentDataContainer inside the chunk's PersistentDataContainer
     */
    private fun save() {
        if (pdc!!.isEmpty) {
            chunk!!.persistentDataContainer.remove(key!!)
        } else {
            chunk!!.persistentDataContainer.set(key!!, PersistentDataType.TAG_CONTAINER, pdc!!)
        }
    }

    override fun <T : Any?, Z : Any> set(key: NamespacedKey, type: PersistentDataType<T, Z>, value: Z) {
        pdc?.set(key, type, value)
        save()
    }

    override fun <T : Any?, Z : Any?> has(key: NamespacedKey, type: PersistentDataType<T, Z>): Boolean {
        return pdc?.has(key, type) ?: false
    }

    override fun <T : Any?, Z : Any?> get(key: NamespacedKey, type: PersistentDataType<T, Z>): Z? {
        return pdc!!.get(key, type)
    }

    override fun <T : Any?, Z : Any> getOrDefault(key: NamespacedKey, type: PersistentDataType<T, Z>, defaultValue: Z): Z {
        return pdc!!.getOrDefault(key, type, defaultValue)
    }

    override fun getKeys(): MutableSet<NamespacedKey> {
        return pdc!!.keys
    }

    override fun remove(key: NamespacedKey) {
        pdc!!.remove(key)
        save()
    }


    override fun isEmpty(): Boolean {
        return pdc!!.isEmpty
    }

    override fun getAdapterContext(): PersistentDataAdapterContext {
        return pdc!!.adapterContext
    }

    companion object {
        private var hasWorldInfoGetMinHeightMethod = false

        fun calcHas() {
            hasWorldInfoGetMinHeightMethod = try {
                Class.forName("org.bukkit.generator.WorldInfo")
                true
            } catch (e: ClassNotFoundException) {
                false
            }
        }
    }
}