package abacus.project.ietf.flexiGrid.mediaChannel;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.common.collect.ImmutableList;

import abacus.project.ietf.flexiGrid.mediaChannel.EffectiveFreqSlot;
import abacus.project.ietf.flexiGrid.mediaChannel.LightPathDestination;
import abacus.project.ietf.flexiGrid.mediaChannel.LightPathSource;
import abacus.project.ietf.flexiGrid.mediaChannel.LinkChannelListType;
import abacus.project.ietf.flexiGrid.mediaChannel.MediaChannelAttributes;

import java.util.List;

/**
 * list of configured light paths
 */
@JsonInclude(Include.NON_NULL)
public class LightPathChannelListType implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * light path channel ID
   */
  private final String lpChannelId;

  /**
   * Source of the network media channel
   */
  private final LightPathSource lightPathSource;

  /**
   * Destination of the network media channel
   */
  private final LightPathDestination lightPathDestination;

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
  public LightPathChannelListType (
    @JsonProperty("lp-channel-id") String lpChannelId,
    @JsonProperty("light-path-source") LightPathSource lightPathSource,
    @JsonProperty("light-path-destination") LightPathDestination lightPathDestination,
    @JsonProperty("effective-freq-slot") EffectiveFreqSlot effectiveFreqSlot,
    @JsonProperty("link-channel") List<LinkChannelListType> linkChannel){
    this.lpChannelId = lpChannelId;
    this.lightPathSource = lightPathSource;
    this.lightPathDestination = lightPathDestination;
    this.effectiveFreqSlot = effectiveFreqSlot;
    this.linkChannel = linkChannel != null ? ImmutableList.copyOf(linkChannel) : ImmutableList.<LinkChannelListType>of();
  }


  @JsonProperty("lp-channel-id")
  public String getLpChannelId(){
    return this.lpChannelId;
  }

  @JsonProperty("light-path-source")
  public LightPathSource getLightPathSource(){
    return this.lightPathSource;
  }

  @JsonProperty("light-path-destination")
  public LightPathDestination getLightPathDestination(){
    return this.lightPathDestination;
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
    return Objects.hash(super.hashCode(), lpChannelId, lightPathSource, lightPathDestination, effectiveFreqSlot, linkChannel);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LightPathChannelListType that = (LightPathChannelListType) o;
    return super.equals(o) &&
       Objects.equals(this.lpChannelId, that.lpChannelId) &&
       Objects.equals(this.lightPathSource, that.lightPathSource) &&
       Objects.equals(this.lightPathDestination, that.lightPathDestination) &&
       Objects.equals(this.effectiveFreqSlot, that.effectiveFreqSlot) &&
       Objects.equals(this.linkChannel, that.linkChannel);
  }

}