package benchmark.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CopyrightInfo {
    private final List<String> copyrights;
    private final List<String> holders;
    private final List<String> authors;

    @JsonCreator
    public CopyrightInfo(
            @JsonProperty("copyrights") List<String> copyrights,
            @JsonProperty("holders") List<String> holders,
            @JsonProperty("authors") List<String> authors) {
        this.copyrights = copyrights != null ? Collections.unmodifiableList(copyrights) : Collections.emptyList();
        this.holders = holders != null ? Collections.unmodifiableList(holders) : Collections.emptyList();
        this.authors = authors != null ? Collections.unmodifiableList(authors) : Collections.emptyList();
    }

    public List<String> getCopyrights() {
        return copyrights;
    }

    public List<String> getHolders() {
        return holders;
    }

    public List<String> getAuthors() {
        return authors;
    }

    @Override
    public String toString() {
        return "CopyrightInfo{" +
                "copyrights=" + copyrights +
                ", holders=" + holders +
                ", authors=" + authors +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CopyrightInfo that = (CopyrightInfo) o;
        return Objects.equals(copyrights, that.copyrights) &&
                Objects.equals(holders, that.holders) &&
                Objects.equals(authors, that.authors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(copyrights, holders, authors);
    }
}