package com.example.android.trackmysleepquality.sleeptracker

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.TextItemViewHolder
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight

/**
 * Important: The "core task" in implementing a RecyclerView is creating the Adapter.
 *
 * The Adapter creates a ViewHolder and FILLS it with data for the RecyclerView to Display.
 */


// We will extend our class with : RecyclerView.Adapter<***>()
// *** Define which view holder to use
// *-* replaced: TextItemViewHolder with SleepNightAdapter.ViewHolder
class SleepNightAdapter : RecyclerView.Adapter<SleepNightAdapter.ViewHolder>() {

    // We are creating a LIST to hold the DATA
    var data = listOf<SleepNight>()
        // By using setter we are informing the RecyclerView that the data it is showing has been updated.
        set(value) {
            // We are assigning a new value to the DATA
            field = value

            // We are asking RecyclerView to REDRAW the complete list with the NEW DATA.
            // Note: We will improve this feature later on, so only update row will be redrawn
            notifyDataSetChanged()
        }

    // Total number of ROWS for the RecyclerView to display
    override fun getItemCount() = data.size

    // onCreateViewHolder() is called when a RecyclerView needs a ViewHolder
    // It returns a ViewHolder based on two parameters:
    // 1. parent: RecyclerView itself
    // 2. viewType: It is used when there are multiple views in the SAME recyclerView, i.e. an image, text views and video
    // *_* replaced = TextItemViewHolder with ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        // 1. layoutInflater knows HOW to create views from XML files.
        // 2. In our situation, context = RecyclerView itself.
        val layoutInflater = LayoutInflater.from(parent.context)

        // 1. Reference to our text_item_view.xml FILE.
        // 2. Parent = ViewGroup = RecyclerView itself.
        // 3. RecyclerView automatically adds our item to the hierarchy, so there is no need for us to use attachToRoot
        // *-* replaced .text_item_view to _text_item_sleep_night.xml file
        // *-* removed casting: as TextView since we are inflating a constraintLayout
        // *-* Warning: If we will not remove the casting "as TextView" then the app will crash!
        val view = layoutInflater
                .inflate(R.layout.list_item_sleep_night, parent, false)

        // It will return view based on our custom class: TextItemViewHolder
        // Please refer to our temporary ViewHolder class in Util.kt file
        // *-* replaced: return TextItemViewHolder(view) with return ViewHolder(view)
        return ViewHolder(view)

    }

    // onBindViewHolder() is used to create a single instance of a ROW
    // It requires the VIEW in which data will be PLACED, and the POSITION of ROW from INCOMING data
    // *-* replaced TextItemViewHolder with
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Actual POSITION of the ROW in the INCOMING data
        val item = data[position]

        val res = holder.itemView.context.resources

        // Converting text for Sleep Length
        // Note, we have conversion function inside the Util.kt file
        holder.sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)

        // Quality
        holder.quality.text = convertNumericQualityToString(item.sleepQuality, res)

        // Image
        holder.qualityImage.setImageResource(when (item.sleepQuality) {
            0 -> R.drawable.ic_sleep_0
            1 -> R.drawable.ic_sleep_1
            2 -> R.drawable.ic_sleep_2
            3 -> R.drawable.ic_sleep_3
            4 -> R.drawable.ic_sleep_4
            5 -> R.drawable.ic_sleep_5
            else -> R.drawable.ic_sleep_active
        })

        // Since our ViewHolder, i.e. text_item_view.xml file contains a <TextView>, thus
        // we will use it to display our data: sleepQuality
        // holder.textView.text = item.sleepQuality.toString()


        // RecyclerView recyclers / reuses view holders.
        // In other words, it reuses the view for the item that is about to scroll on the screen.
        // if (item.sleepQuality <= 1) {
        // SET: Custom Color
        // holder.textView.setTextColor(Color.RED)
        // } else {
        // RESET: Default Color
        // holder.textView.setTextColor(Color.BLACK)
        // }

    }


    // It will contain references to all UI Elements inside @res/layout/list_item_sleep_night.xml file
    // These references will be used SleepNightAdapter
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val sleepLength: TextView = itemView.findViewById(R.id.sleep_length)
        val quality: TextView = itemView.findViewById(R.id.quality_string)
        val qualityImage: ImageView = itemView.findViewById(R.id.quality_image)
    }

}