package cz.mendelu.xlinek.project_01.files;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Objects;

@Setter @Getter
class Planes {
    HashMap<String, String> planes;

    @JsonCreator
    Planes(@JsonProperty("planes") HashMap<String, String> planes) {
        this.planes = planes;
    }

    Planes(){

    }

    @Override
    public String toString() {
        return "planes=" + planes + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Planes planes1 = (Planes) o;
        return Objects.equals(planes, planes1.planes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planes);
    }
}
