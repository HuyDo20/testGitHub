package com.example.fileaccesser;




import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    Context context;
    File[] filesAndFolders;

    public MyAdapter(Context context, File[] filesAndFolders){
        this.context = context;
        this.filesAndFolders = filesAndFolders;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.activity_my_adapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {

        File selectedFile = filesAndFolders[position];
        holder.textView.setText(selectedFile.getName());

        if(selectedFile.isDirectory()){
            holder.imageView.setImageResource(R.drawable.ic_launcher_background);
        }else{
            holder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFile.isDirectory()) {
                    Intent intent = new Intent(context, FileListActivity.class);
                    String path = selectedFile.getAbsolutePath();
                    intent.putExtra("path", path);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    try {
//                        Intent intent = new Intent();
//                        intent.setAction(android.content.Intent.ACTION_VIEW);
                        switchCase(selectedFile.getAbsolutePath()); // Get MIME type

//                        if (type != null) {
//                            intent.setDataAndType(Uri.parse(selectedFile.getAbsolutePath()), type);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(intent);
//                        } else {
//                            Toast.makeText(context.getApplicationContext(), "Cannot open the file", Toast.LENGTH_SHORT).show();
//                        }
                    } catch (Exception e) {
                        Toast.makeText(context.getApplicationContext(), "Cannot open the file", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            private void switchCase(String filePath) {
                String ext = MimeTypeMap.getFileExtensionFromUrl(filePath);

                Log.d("filePath", filePath);
                Log.d("file type", ext);
                String fileType = filePath.substring(filePath.lastIndexOf(".")+1);

                if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("png")) {
                        Log.d("case image", "");
                        Intent intent = new Intent(context, ViewImageActivity.class);
                        intent.putExtra("filePath", filePath);
                        context.startActivity(intent);
                } else if (fileType.equalsIgnoreCase("mp4") || fileType.equalsIgnoreCase("3gp")) {
                        Log.d("case mp4", "");
                        Intent intent = new Intent(context, VideoPlayActivity.class);
                        intent.putExtra("filePath", filePath);
                        context.startActivity(intent);
                } else if (ext.equalsIgnoreCase("txt")) {
                        Intent intent = new Intent(context, ViewTextActivity.class);
                        intent.putExtra("filePath", filePath);
                        context.startActivity(intent);
                }else if (fileType.equalsIgnoreCase("mp3") || fileType.equalsIgnoreCase("wav")){
                        Log.d("case mp3", "");
                        Intent intent = new Intent(context, MusicPlayActivity.class);
                        intent.putExtra("filePath", filePath);
                        context.startActivity(intent);
                    }
                }

        });

}

    @Override
    public int getItemCount() {
        return filesAndFolders.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.file_name_text_view);
            imageView = itemView.findViewById(R.id.icon_view);
        }
    }
}
