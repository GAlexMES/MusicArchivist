package de.brennecke.musicarchivst.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.brennecke.musicarchivst.R;
import de.brennecke.musicarchivst.model.Track;
import de.brennecke.musicarchivst.model.Tracklist;

/**
 * Created by Alexander on 02.01.2016.
 */
public class TracklistHandler {

    public static void update(TableLayout trackListLayout, Tracklist tracklist, Context context){

        List<Track> tracks = tracklist.getTracks();
        List<View> trackViews = new ArrayList<>();
        for (Track t : tracks){
            View trackView = createTrackView(t, context);
            trackListLayout.addView(trackView);
        }
    }

    private static View createTrackView(Track track, Context context){
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.tracklist_item, null);

        TextView track_number = (TextView) v.findViewById(R.id.track_number);
        TextView track_title = (TextView) v.findViewById(R.id.track_title);
        TextView track_duration = (TextView) v.findViewById(R.id.track_duration);

        track_number.setText(String.valueOf(track.getTrackNumber()));
        track_title.setText(track.getName());
        track_duration.setText(getStringFromDuration(track.getDuration()));

        return v;
    }

    private static String getStringFromDuration(int duration){
        int seconds =  duration%60;
        int minutes = (duration-seconds)/60;
        if(seconds<10){
            return minutes+":0"+seconds;
        }
        return minutes+":"+seconds;
    }
}
