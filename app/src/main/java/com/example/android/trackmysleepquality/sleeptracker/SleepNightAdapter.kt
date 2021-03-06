package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.TextItemViewHolder
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Important: The "core task" in implementing a RecyclerView is creating the Adapter.
 *
 * The Adapter creates a ViewHolder and FILLS it with data for the RecyclerView to Display.
 */

// We will extend our class with : RecyclerView.Adapter<***>()
// And use SleepNightAdapter.ViewHolder so it can use our NESTED ViewHolder
// class SleepNightAdapter : RecyclerView.Adapter<SleepNightAdapter.ViewHolder>() {


// Constants for different View Types
private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

/**
 * Issue: Currently our constuctor is only allowed to use only 1 type of the viewHolder,
 * class SleepNightAdapter(val clickListener: SleepNightListener) : ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()) {
 */

/**
 * We will change the constructor so it can support multiple ViewHolders
 */
class SleepNightAdapter(val clickListener: SleepNightListener) : ListAdapter<DataItem, RecyclerView.ViewHolder>(SleepNightDiffCallback()) {

    // onCreateViewHolder() is called when a RecyclerView needs a ViewHolder
    // It returns a ViewHolder based on two parameters:
    // 1. parent: RecyclerView itself
    // 2. viewType: It is used when there are multiple views in the SAME recyclerView, i.e. an image, text views and video

    // override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        // return ViewHolder.from(parent)

        // Conditional Return
        return when (viewType) {

            // Returns Header based on header.xml file
            // It depends on: "companion object" inside the
            // "class TextViewHolder(view: View) : RecyclerView.ViewHolder(view)"
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)

            // Returns ViewHolder for a Single row of data
            // It depends on: "companion object" inside the
            // class ViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root) {
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)

            // Error Handling
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }

    }

    // Checks which type of item should be returned?
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {

            // Returns HEADER
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER

            // Returns DATA for the ROW
            is DataItem.SleepNightItem -> ITEM_VIEW_TYPE_ITEM

            // Error Handling
            else -> throw ClassCastException("Unknown position ${position}")
        }
    }

    // Instead of running our rendering function on the Main UI Thread, we are taking
    // advantage of Coroutines to handle rendering ViewHolders and process data on other threads.
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<SleepNight>?) {

        // Wrapper for our Coroutine's function
        adapterScope.launch {
            val items = when (list) {

                // If the record is a the 0-index
                null -> listOf(DataItem.Header)

                // If the record is not at the 0-index, then populate the list in RecylerView
                else -> listOf(DataItem.Header) + list.map { DataItem.SleepNightItem(it) }

            }

            withContext(Dispatchers.Main) {
                // It is a feature provided by the ListAdapter
                submitList(items)
            }
        }

    }

    // onBindViewHolder() is used to create a single instance of a ROW
    // It requires the VIEW in which data will be PLACED, and the POSITION of ROW from INCOMING data
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        // Conditional Statement
        when (holder) {

            // Only assign data to the ViewHolder if the holder is of a ViewHolder type,
            // i.e. it contains the DATA and it does not have any refence to the header.xml file
            is ViewHolder -> {
                // Actual POSITION of the ROW in the INCOMING data
                // val item = getItem(position)
                // holder.bind(item!!, clickListener)

                // Cast the object returned type by getItem() as DataItem.SleepNightItem
                val nightItem = getItem(position) as DataItem.SleepNightItem

                // Binds the Data for the row
                // It depends on our information retrieved from sleepNight in our sealed class
                holder.bind(nightItem.sleepNight, clickListener)

            }

        }
    }

    // It will contain references to all UI Elements inside @res/layout/list_item_sleep_night.xml file
    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root) {

        // * onBindViewHolder
        // fun bind(item: SleepNight) {
        fun bind(item: SleepNight, clickListener: SleepNightListener) {

            // We need to tell the binding object about our new SleepNight record.
            binding.sleep = item

            // Execute clickListener function
            binding.clickListener = clickListener

            // Speed optimization: We are asking to execute any pending bindings right now.
            // It is a best practice to use this feature as it can slightly speed up sizing the views.
            binding.executePendingBindings()

            // We are moving the link to references inside the bind() function
            // Reference to all resources in our ViewHolder
            // val res = itemView.context.resources

            // Converting text for Sleep Length
            // Note, we have conversion function inside the Util.kt file
            // binding.sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)

            // Quality
            // binding.qualityString.text = convertNumericQualityToString(item.sleepQuality, res)

            // Image
            // binding.qualityImage.setImageResource(when (item.sleepQuality) {
            //    0 -> R.drawable.ic_sleep_0
            //    1 -> R.drawable.ic_sleep_1
            //    2 -> R.drawable.ic_sleep_2
            //    3 -> R.drawable.ic_sleep_3
            //    4 -> R.drawable.ic_sleep_4
            //    5 -> R.drawable.ic_sleep_5
            //    else -> R.drawable.ic_sleep_active
            // })


        }

        // * onCreateViewHolder
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                // 1. layoutInflater knows HOW to create views from XML files.
                // 2. In our situation, context = RecyclerView itself.
                // 3. Reference to our list_item_sleep_night.xml FILE.
                // 4. Parent = ViewGroup = RecyclerView itself.
                // 5. RecyclerView automatically adds our item to the hierarchy, so there is no need for us to use attachToRoot
                // val view = LayoutInflater.from(parent.context) .inflate(R.layout.list_item_sleep_night, parent, false)

                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)

                // It will return view based on our custom class: TextItemViewHolder
                // Please refer to our temporary ViewHolder class in Util.kt file
                // *-* replaced: return TextItemViewHolder(view) with return ViewHolder(view)
                // return ViewHolder(view)

                return ViewHolder(binding)
            }
        }
    }


    // It will inflate the header.xml layout file and return an instance of TextViewHolder
    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        companion object {
            fun from(parent: ViewGroup): TextViewHolder {

                // We are establishing current CONTEXT
                val layoutInflater = LayoutInflater.from(parent.context)

                // Reference to our res/layout/header.xml file
                val view = layoutInflater.inflate(R.layout.header, parent, false)

                // Returns the instance of TextViewHolder
                return TextViewHolder(view)
            }
        }

    }

}

// This top-level class extends DiffUtil.ItemCallback<>()
// We are using DataItem as a generic parameter

// class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>() {

class SleepNightDiffCallback : DiffUtil.ItemCallback<DataItem>() {

    // DiffUtils uses these two methods to figure out how the list and items have changed.

    // Change in: List of Items?
    // override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {

        // Returns TRUE if they items have same nightId
        // This step is used to identify if an item was (1) added (2) removed or (3) moved
        // return oldItem.nightId == newItem.nightId

        return oldItem.id == newItem.id

    }

    // Change in: Content?
    // override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {

        // If there are difference between fields, then it means that the content was updated
        return oldItem == newItem
    }

}

/**
 * OnClick Listener for the a single item in the grid (RecyclerView)
 */
class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {
    fun onClick(night: SleepNight) = clickListener(night.nightId)
}

/**
 * Sealed Class (Closed Type)
 * It means that all sub-classes of it must be defined in this file.
 * This ensure that the total number of subclasses are known to the compiler.
 */
sealed class DataItem {

    // We are here declaring the type fo id variable
    abstract val id: Long

    // Data Type - SleepNight
    // It is a wrapper around a SleepNight as it Contains list of the actual data
    data class SleepNightItem(val sleepNight: SleepNight) : DataItem() {
        override val id = sleepNight.nightId
    }

    // It will contain reference to our custom header
    // We are using Object as there will be only 1 instance of Header and it will not contain any data
    object Header : DataItem() {
        // We are using .MIN_VALUE so due to nature of its less amount,
        // so it can never conflict with unique nightID keys
        override val id = Long.MIN_VALUE
    }

}