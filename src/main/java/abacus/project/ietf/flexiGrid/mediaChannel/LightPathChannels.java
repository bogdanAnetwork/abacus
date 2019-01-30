package abacus.project.ietf.flexiGrid.mediaChannel;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.common.collect.ImmutableList;

import abacus.project.ietf.flexiGrid.mediaChannel.LightPathChannelListType;

import java.util.List;

/**
 * this is a light path that transports a wavelength ... 
 */
@JsonInclude(Include.NON_NULL)
public class LightPathChannels implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * list of configured light paths
   */
  private final List<LightPathChannelListType> lightPathChannel;


  @JsonCreator
  public LightPathChannels (
    @JsonProperty("light-path-channel") List<LightPathChannelListType> lightPathChannel){
    this.lightPathChannel = lightPathChannel != null ? ImmutableList.copyOf(lightPathChannel) : ImmutableList.<LightPathChannelListType>of();
  }


  @JsonProperty("light-path-channel")
  public List<LightPathChannelListType> getLightPathChannel(){
    return this.lightPathChannel;
  }


  @Override
  public int hashCode() {
    return Objects.hash(lightPathChannel);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LightPathChannels that = (LightPathChannels) o;
    return Objects.equals(this.lightPathChannel, that.lightPathChannel);
  }

}