package com.android.demo.nlpdemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SmsDecodeContentAdapter(val context: Context, val decoderList: ArrayList<DecoderBean>) : RecyclerView.Adapter<SmsDecodeContentAdapter.SmsDecodeContentHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsDecodeContentHolder {
        return SmsDecodeContentHolder(LayoutInflater.from(context).inflate(R.layout.item_decode_content, parent, false))
    }

    override fun onBindViewHolder(holder: SmsDecodeContentHolder, position: Int) {
        holder.tvContent.text = decoderList[position].smsContent
        holder.tvDecodeRes.text = decoderList[position].decoderRes
    }

    override fun getItemCount(): Int {
        return decoderList.size
    }

    class SmsDecodeContentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvContent: TextView
        var tvDecodeRes: TextView

        init {
            tvContent = itemView.findViewById(R.id.tv_content)
            tvDecodeRes = itemView.findViewById(R.id.tv_decode_res)
        }
    }


}