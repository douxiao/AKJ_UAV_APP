package videoview;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;

import io.vov.vitamio.MediaPlayer;

public class PhotoSaver {
    String filename;
    Date date;
    public static String finalname;
    String path1;
    Bitmap image;
    Calendar rightNow;
    MediaPlayer mMediaPlayer;
    Context context;
    public static String imgname;

    public PhotoSaver(Context c, MediaPlayer m) {
        this.context = c;
        this.mMediaPlayer = m;
        rightNow = Calendar.getInstance();
        filename = rightNow.get(Calendar.DAY_OF_MONTH) + "_" + (rightNow.get(Calendar.MONTH) + 1) + "_" + rightNow.get(Calendar.YEAR) + ".jpeg";


    }

    /**
     * Get the current frame of the MediaPlayer and saves it in the local storage of the phone at
     * the PNG format.
     */
    public void record() {
        if (Environment.getExternalStorageState() != null) {
            try {
                image = mMediaPlayer.getCurrentFrame();

                File picture = getOutputMediaFile();
                FileOutputStream fos = new FileOutputStream(picture);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
                Toast.makeText(context, "Photo saved :" + imgname, Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                Toast.makeText(context, "权限不够，请开启手机的权限管理！", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Toast.makeText(context, "Echec close", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Repertoire non disponible", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Initializes the File which will be used to save the frame. Called in the record() method.
     *
     * @return the File to be used.
     */
    public File getOutputMediaFile() {
        rightNow = Calendar.getInstance();
        finalname = "DronePicture_" + rightNow.get(Calendar.HOUR) + ":" + rightNow.get(Calendar.MINUTE) + ":" + rightNow.get(Calendar.SECOND) + "_" + filename;
        //Create a media file name
        File mediaFile;
        imgname = Environment.getExternalStorageDirectory() + "/Pictures/" + finalname;
       // imgname = Environment.getExternalStorageDirectory() + "/Pictures/" + "snapshot" + System.currentTimeMillis() + ".jpeg";
        mediaFile = new File(imgname);
        return mediaFile;
    }



}