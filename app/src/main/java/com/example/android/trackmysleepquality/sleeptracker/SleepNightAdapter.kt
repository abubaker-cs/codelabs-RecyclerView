package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.TextItemViewHolder
import com.example.android.trackmysleepquality.database.SleepNight

/**
 * Important: The "core task" in implementing a RecyclerView is creating the Adapter.
 *
 * The Adapter creates a ViewHolder and FILLS it with data for the RecyclerView to Display.
 */

// We will extend our class with : RecyclerView.Adapter<***>()
// *** Define which view holder to use
class SleepNightAdapter : RecyclerView.Adapter<TextItemViewHolder>() {

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


    // onBindViewHolder() is used to create a single instance of a ROW
    // It requires the VIEW in which data will be PLACED, and the POSITION of ROW from INCOMING data
    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {

        // Actual POSITION of the ROW in the INCOMING data
        val item = data[position]

        // Since our ViewHolder, i.e. text_item_view.xml file contains a <TextView>, thus
        // we will use it to display our data: sleepQuality
        holder.textView.text = item.sleepQuality.toString()

    }

    // onCreateViewHolder() is called when a RecyclerView needs a ViewHolder
    // It returns a ViewHolder based on two parameters:
    // 1. parent: RecyclerView itself
    // 2. viewType: It is used when there are multiple views in the SAME recyclerView, i.e. an image, text views and video
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {

        // 1. layoutInflater knows HOW to create views from XML files.
        // 2. In our situation, context = RecyclerView itself.
        val layoutInflater = LayoutInflater.from(parent.context)

        // 1. Reference to our text_item_view.xml FILE.
        // 2. Parent = ViewGroup = RecyclerView itself.
        // 3. RecyclerView automatically adds our item to the hierarchy, so there is no need for us to use attachToRoot
        val view = layoutInflater
                .inflate(R.layout.text_item_view, parent, false) as TextView

        // It will return view based on our custom class: TextItemViewHolder
        // Please refer to our temporary ViewHolder class in Util.kt file
        return TextItemViewHolder(view)

    }

}