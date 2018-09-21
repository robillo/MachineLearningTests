package com.assistiveapps.machinelearningtests.main.rv;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.assistiveapps.machinelearningtests.R;
import com.assistiveapps.machinelearningtests.tests.barcode_scan.BarcodeScanActivity;
import com.assistiveapps.machinelearningtests.tests.face_detect.FaceDetectActivity;
import com.assistiveapps.machinelearningtests.tests.image_label.ImageLabelActivity;
import com.assistiveapps.machinelearningtests.tests.text_scan.TextScanActivity;

import java.util.List;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.OptionsHolder> {

    private List<OptionsData> list;
    private Context context;

    public OptionsAdapter(List<OptionsData> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public OptionsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new OptionsHolder(
                LayoutInflater.from(context).inflate(R.layout.row_options, parent, false)
        );
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    public void onBindViewHolder(@NonNull OptionsHolder optionsHolder, final int position) {
        optionsHolder.nameText.setText(list.get(position).getOption_name());

        final int pos = position;

        optionsHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (list.get(pos).getOption_name()) {
                    case "BARCODE SCANNING": {
                        context.startActivity(new Intent(context, BarcodeScanActivity.class));
                        break;
                    }
                    case "TEXT SCANNING": {
                        context.startActivity(new Intent(context, TextScanActivity.class));
                        break;
                    }
                    case "IMAGE LABELLING": {
                        context.startActivity(new Intent(context, ImageLabelActivity.class));
                        break;
                    }
                    case "FACE DETECTION": {
                        context.startActivity(new Intent(context, FaceDetectActivity.class));
                        break;
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class OptionsHolder extends RecyclerView.ViewHolder {

        TextView nameText;
        ImageView nameImage;

        OptionsHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.name_text);
            nameImage = itemView.findViewById(R.id.name_image);
        }
    }
}
