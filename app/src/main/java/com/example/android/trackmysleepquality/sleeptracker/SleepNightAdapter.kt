package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding

/**
 * Important: The "core task" in implementing a RecyclerView is creating the Adapter.
 *
 * The Adapter creates a ViewHolder and FILLS it with data for the RecyclerView to Display.
 */


// We will extend our class with : RecyclerView.Adapter<***>()
// And use SleepNightAdapter.ViewHolder so it can use our NESTED ViewHolder
// class SleepNightAdapter : RecyclerView.Adapter<SleepNightAdapter.ViewHolder>() {
class SleepNightAdapter : ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()) {

    // onCreateViewHolder() is called when a RecyclerView needs a ViewHolder
    // It returns a ViewHolder based on two parameters:
    // 1. parent: RecyclerView itself
    // 2. viewType: It is used when there are multiple views in the SAME recyclerView, i.e. an image, text views and video
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent)

    }

    // onBindViewHolder() is used to create a single instance of a ROW
    // It requires the VIEW in which data will be PLACED, and the POSITION of ROW from INCOMING data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Actual POSITION of the ROW in the INCOMING data
        val item = getItem(position)
        holder.bind(item)

    }

    // It will contain references to all UI Elements inside @res/layout/list_item_sleep_night.xml file
    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root) {

        // * onBindViewHolder
        fun bind(item: SleepNight) {

            // We are moving the link to references inside the bind() function
            // Reference to all resources in our ViewHolder
            val res = itemView.context.resources

            // Converting text for Sleep Length
            // Note, we have conversion function inside the Util.kt file
            binding.sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)

            // Quality
            binding.qualityString.text = convertNumericQualityToString(item.sleepQuality, res)

            // Image
            binding.qualityImage.setImageResource(when (item.sleepQuality) {
                0 -> R.drawable.ic_sleep_0
                1 -> R.drawable.ic_sleep_1
                2 -> R.drawable.ic_sleep_2
                3 -> R.drawable.ic_sleep_3
                4 -> R.drawable.ic_sleep_4
                5 -> R.drawable.ic_sleep_5
                else -> R.drawable.ic_sleep_active
            })
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

}

// This top-level class extends DiffUtil.ItemCallback<>()
// We are using SleepNight as a generic parameter
class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>() {

    // DiffUtils uses these two methods to figure out how the list and items have changed.

    // Change in: List of Items?
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {

        // Returns TRUE if they items have same nightId
        // This step is used to identify if an item was (1) added (2) removed or (3) moved
        return oldItem.nightId == newItem.nightId
    }

    // Change in: Content?
    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {

        // If there are difference between fields, then it means that the content was updated
        return oldItem == newItem
    }

}