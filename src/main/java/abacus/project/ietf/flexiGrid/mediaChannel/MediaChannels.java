package abacus.project.ietf.flexiGrid.mediaChannel;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.common.collect.ImmutableList;

import abacus.project.ietf.flexiGrid.mediaChannel.MediaChannelListType;

import java.util.List;

/**
 * Media association that represents both the topology
 * (i.e., path through the media) and the resource
 * (frequency slot) that it occupies.  As a topological
 * construct, it represents a (effective) frequency slot
 * supported by a concatenation of media elements (fibers,
 * amplifiers, filters, switching matrices...).  This term
 * is used to identify the end-to-end physical layer entity
 * with its corresponding (one or more) frequency slots
 * local at each link filters.
 */
@JsonInclude(Include.NON_NULL)
public class MediaChannels implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * list of media channels configured
   */
  private final List<MediaChannelListType> mediaChannel;


  @JsonCreator
  public MediaChannels (
    @JsonProperty("media-channel") List<MediaChannelListType> mediaChannel){
    this.mediaChannel = mediaChannel != null ? ImmutableList.copyOf(mediaChannel) : ImmutableList.<MediaChannelListType>of();
  }


  @JsonProperty("media-channel")
  public List<MediaChannelListType> getMediaChannel(){
    return this.mediaChannel;
  }


  @Override
  public int hashCode() {
    return Objects.hash(mediaChannel);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MediaChannels that = (MediaChannels) o;
    return Objects.equals(this.mediaChannel, that.mediaChannel);
  }

}