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

import java.util.List;

/**
 * Set of attributes of a media channel
 */
@JsonInclude(Include.NON_NULL)
public class MediaChannelAttributes implements Serializable {

  private static final long serialVersionUID = 1L;

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
  public MediaChannelAttributes (
    @JsonProperty("effective-freq-slot") EffectiveFreqSlot effectiveFreqSlot,
    @JsonProperty("link-channel") List<LinkChannelListType> linkChannel){
    this.effectiveFreqSlot = effectiveFreqSlot;
    this.linkChannel = linkChannel != null ? ImmutableList.copyOf(linkChannel) : ImmutableList.<LinkChannelListType>of();
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
    return Objects.hash(effectiveFreqSlot, linkChannel);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MediaChannelAttributes that = (MediaChannelAttributes) o;
    return Objects.equals(this.effectiveFreqSlot, that.effectiveFreqSlot) &&
       Objects.equals(this.linkChannel, that.linkChannel);
  }

}