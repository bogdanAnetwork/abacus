package abacus.project.ietf.flexiGrid.mediaChannel;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.common.collect.ImmutableList;

import abacus.project.ietf.flexiGrid.mediaChannel.EffectiveFreqSlot;
import abacus.project.ietf.flexiGrid.mediaChannel.LinkChannelListType;
import abacus.project.ietf.flexiGrid.mediaChannel.MediaChannelAttributes;
import abacus.project.ietf.flexiGrid.mediaChannel.MediaChannelDestination;
import abacus.project.ietf.flexiGrid.mediaChannel.MediaChannelSource;

import java.util.List;

/**
 * list of media channels configured
 */
@JsonInclude(Include.NON_NULL)
public class MediaChannelListType implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * channel ID
   */
  private final String channelId;

  /**
   * Source of the media channel
   */
  private final MediaChannelSource mediaChannelSource;

  /**
   * Destination of the media channel
   */
  private final MediaChannelDestination mediaChannelDestination;

  /**
   * The effective frequency slot is an attribute of
   * a media channel and, being a frequency slot, it is
   * described by its nominal central frequency and slot
   * width
   */
  private final EffectiveFreqSlot effectiveFreqSlot;

  /**
   * A list of the concatenated elements of the media
   * channel.
   */
  private final List<LinkChannelListType> linkChannel;


  @JsonCreator
  public MediaChannelListType (
    @JsonProperty("channel-id") String channelId,
    @JsonProperty("media-channel-source") MediaChannelSource mediaChannelSource,
    @JsonProperty("media-channel-destination") MediaChannelDestination mediaChannelDestination,
    @JsonProperty("effective-freq-slot") EffectiveFreqSlot effectiveFreqSlot,
    @JsonProperty("link-channel") List<LinkChannelListType> linkChannel){
    this.channelId = channelId;
    this.mediaChannelSource = mediaChannelSource;
    this.mediaChannelDestination = mediaChannelDestination;
    this.effectiveFreqSlot = effectiveFreqSlot;
    this.linkChannel = linkChannel != null ? ImmutableList.copyOf(linkChannel) : ImmutableList.<LinkChannelListType>of();
  }


  @JsonProperty("channel-id")
  public String getChannelId(){
    return this.channelId;
  }

  @JsonProperty("media-channel-source")
  public MediaChannelSource getMediaChannelSource(){
    return this.mediaChannelSource;
  }

  @JsonProperty("media-channel-destination")
  public MediaChannelDestination getMediaChannelDestination(){
    return this.mediaChannelDestination;
  }

  @JsonProperty("effective-freq-slot")
  public EffectiveFreqSlot getEffectiveFreqSlot(){
    return this.effectiveFreqSlot;
  }

  @JsonProperty("link-channel")
  public List<LinkChannelListType> getLinkChannel(){
    return this.linkChannel;
  }


  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), channelId, mediaChannelSource, mediaChannelDestination, effectiveFreqSlot, linkChannel);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MediaChannelListType that = (MediaChannelListType) o;
    return super.equals(o) &&
       Objects.equals(this.channelId, that.channelId) &&
       Objects.equals(this.mediaChannelSource, that.mediaChannelSource) &&
       Objects.equals(this.mediaChannelDestination, that.mediaChannelDestination) &&
       Objects.equals(this.effectiveFreqSlot, that.effectiveFreqSlot) &&
       Objects.equals(this.linkChannel, that.linkChannel);
  }

}