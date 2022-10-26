package com.android.demo.nlpdemo

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.alpha
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import kotlin.random.Random

class SmsDecodeContentAdapter(val context: Context, val decoderList: ArrayList<DecoderBean>) : RecyclerView.Adapter<SmsDecodeContentAdapter.SmsDecodeContentHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsDecodeContentHolder {
        return SmsDecodeContentHolder(LayoutInflater.from(context).inflate(R.layout.item_decode_content, parent, false))
    }

    override fun onBindViewHolder(holder: SmsDecodeContentHolder, position: Int) {
        holder.tvContent.text = decoderList[position].smsContent
        holder.tvDecodeRes.text = decoderList[position].decoderRes
        holder.tvNo.text = "${position}."
    }

    override fun getItemCount(): Int {
        return decoderList.size
    }

    class SmsDecodeContentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvContent: TextView
        var tvDecodeRes: TextView
        var tvNo: TextView
        var cvRootView: CardView

        init {
            tvContent = itemView.findViewById(R.id.tv_content)
            tvDecodeRes = itemView.findViewById(R.id.tv_decode_res)
            tvNo = itemView.findViewById(R.id.tv_no)
            cvRootView = itemView.findViewById(R.id.cv_root_view)
        }
    }


}