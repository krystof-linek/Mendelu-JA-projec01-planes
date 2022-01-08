package cz.mendelu.xlinek.project_01.files;

import lombok.Getter;

import java.util.List;

@Getter
public class Result {
    private final String id;
    private final String from;
    private final List<String> track;
    private final String to;
    private final int time;

    public Result(String id, String from, List<String> track, String to, int time){
        this.id = id;
        this.from = from;
        this.track = track;
        this.to = to;
        this.time = time;
    }

    @Override
    public String toString() {
        return track + " => ";
    }

}
