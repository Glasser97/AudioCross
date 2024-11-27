package com.grayson.audiocross.domain.common

import androidx.annotation.RequiresApi
import java.util.Comparator
import java.util.function.Predicate
import java.util.function.UnaryOperator

/**
 * Observable ArrayList
 */
class ObservableArrayList<T>(initialCapacity: Int = 0) : ArrayList<T>(initialCapacity) {

    private val observerList: MutableList<ListObserver<T>> = mutableListOf()

    // region define

    /**
     * 列表观察者
     */
    open class ListObserver<T> {
        /**
         * 列表内容发生变化，但不确定是什么变化
         */
        open fun onChanged(source: ObservableArrayList<T>) {
            // Do nothing
        }

        /**
         * 列表部分内容发生变化
         * @param start 变化范围的起始坐标
         * @param count 变化范围的大小
         */
        open fun onItemRangeChanged(source: ObservableArrayList<T>, start: Int, count: Int) {
            // do nothing
        }

        /**
         * 列表发生 Item 插入
         * @param start 插入位置
         * @param count 插入个数
         */
        open fun onItemRangeInserted(source: ObservableArrayList<T>, start: Int, count: Int) {
            // do nothing
        }

        /**
         * 列表发生 Item 移除
         * @param start 移除位置
         * @param count 移除个数
         */
        open fun onItemRangeRemoved(source: ObservableArrayList<T>, start: Int, count: Int) {
            // do nothing
        }

        /**
         * 列表发生 Item 移动
         * @param from 移动前的位置
         * @param to 要移动到这里去（这是移动前的坐标哦）
         */
        open fun onItemMoved(source: ObservableArrayList<T>, from: Int, to: Int) {
            // do nothing
        }
    }

    // endregion

    // region interface

    /**
     * 添加观察者
     */
    fun addObserver(listener: ListObserver<T>) {
        this.observerList.add(listener)
    }

    /**
     * 移除观察者
     */
    fun removeObserver(listener: ListObserver<T>) {
        this.observerList.remove(listener)
    }

    /**
     * 移动 Item
     * @param from 要移动的段的起始坐标
     * @param to 要移动到这里去（这是移动前的坐标哦）
     */
    fun move(from: Int, to: Int) {
        val temp = this[from]
        super.removeAt(from)
        super.add(to, temp)
        notifyItemMoved(from, to)
    }

    /**
     * 重置数据。
     * 如果先 clear，在 addAll，会触发两次变化通知，这种清空下可以用此 API 来代替。
     * 后面考虑：是否引入变化对比（sdk 已有，叫 DiffUtil，）
     */
    fun reset(elements: List<T>?)
    {
//        val diffResult = DiffUtil.calculateDiff(DefaultDiffCallback(this, elements))
//        diffResult.dispatchUpdatesTo(this)

        if (this !== elements) {
            super.clear()

            elements?.let {
                super.addAll(it)
            }
        }

        notifyChanged()
    }

    /**
     * 通知指定 item 的内容发生变化
     * @param index 索引
     */
    fun notifyAt(index: Int) {
        notifyItemRangeChanged(index, 1)
    }

    /**
     * 通知指定 item 的内容发生变化
     * @param index 索引
     * @param count 变化的个数
     */
    fun notifyRange(index: Int, count: Int) {
        notifyItemRangeChanged(index, count)
    }

    // endregion

    // region override ArrayList（语义和 ArrayList 对应方法一致）

    override fun add(element: T): Boolean {
        val added = super.add(element)
        if (added) {
            notifyItemRangeInserted(this.size - 1, 1)
        }
        return added
    }

    override fun add(index: Int, element: T) {
        super.add(index, element)
        notifyItemRangeInserted(index, 1)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val oldSize = this.size
        val added = super.addAll(elements)
        if (added) {
            notifyItemRangeInserted(oldSize, this.size - oldSize)
        }
        return added
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val added = super.addAll(index, elements)
        if (added) {
            notifyItemRangeInserted(index, elements.size)
        }
        return added
    }

    override fun clear() {
        val oldSize = this.size
        super.clear()
        if (oldSize > 0) {
            notifyItemRangeRemoved(0, oldSize)
        }
    }

    override fun removeAt(index: Int): T {
        val element = super.removeAt(index)
        notifyItemRangeRemoved(index, 1)
        return element
    }

    override fun remove(element: T): Boolean {
        val index = this.indexOf(element)
        return if (index >= 0) {
            this.removeAt(index)
            true
        } else {
            false
        }
    }

    public override fun removeRange(fromIndex: Int, toIndex: Int) {
        super.removeRange(fromIndex, toIndex)
        notifyItemRangeRemoved(fromIndex, toIndex - fromIndex)
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        val changed = super.removeAll(elements)
        if (changed) {
            notifyChanged()
        }
        return changed
    }

    @RequiresApi(24)
    override fun removeIf(filter: Predicate<in T>): Boolean {
        val changed = super.removeIf(filter)
        if (changed) {
            notifyChanged()
        }
        return changed
    }

    override fun set(index: Int, element: T): T {
        val old = super.set(index, element)
        notifyItemRangeChanged(index, 1)
        return old
    }

    @RequiresApi(24)
    override fun replaceAll(operator: UnaryOperator<T>) {
        super.replaceAll(operator)
        notifyChanged()
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val changed = super.retainAll(elements)
        if (changed) {
            notifyChanged()
        }
        return changed
    }

    @RequiresApi(24)
    override fun sort(c: Comparator<in T>?) {
        // 这里有斜线，应该是 IDE 的 bug，测试过实际上是调用到了基类的 sort 方法了，kotlin 官方说明也是说优先基类的，而不是扩展方法~
        super<ArrayList>.sort(c)
        notifyChanged()
    }

    // endregion

    // region notify

    private fun notifyItemRangeInserted(start: Int, count: Int) {
        this.observerList.forEach {
            it.onItemRangeInserted(this, start, count)
        }
    }

    private fun notifyItemRangeRemoved(start: Int, count: Int) {
        this.observerList.forEach {
            it.onItemRangeRemoved(this, start, count)
        }
    }

    private fun notifyItemRangeChanged(start: Int, count: Int) {
        this.observerList.forEach {
            it.onItemRangeChanged(this, start, count)
        }
    }

    private fun notifyChanged() {
        this.observerList.forEach {
            it.onChanged(this)
        }
    }

    private fun notifyItemMoved(from: Int, to: Int) {
        this.observerList.forEach {
            it.onItemMoved(this, from, to)
        }
    }

    // endregion
}