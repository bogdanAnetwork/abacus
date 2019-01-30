package abacus.project.ietf.extensions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import com.google.common.collect.ImmutableMap;

import abacus.project.OpticParam.ChNr;
import abacus.project.OpticParam.ChSp;
import abacus.project.OpticParam.ParamOption;
import abacus.project.acc400Dev.intern.Parameters;
import abacus.project.ietf.flexiGrid.mediaChannel.MediaChannelListType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * wrapper class for ietf media channel adding extra methods 
 * @author BMA
 *
 */
public class MediaChannelWrapper {
    public MediaChannelListType mc;
    private ObjectMapper mapper = new ObjectMapper();

    public MediaChannelWrapper(MediaChannelListType mc) {
        this.mc = mc;
    }

    /**
     * 
     * @return a hashmap with the nodeIDs and the specific configuration format
     *         : ImmutableMap<Parameters, ParamOption>
     */
    public HashMap<String, ImmutableMap<Parameters, ParamOption>> getImaps() {
        // store all nodeIDs as string on the media channel path
        HashMap<String, ImmutableMap<Parameters, ParamOption>> nodeIDs = new HashMap<String, ImmutableMap<Parameters, ParamOption>>();
        // TODO: for now, a media channel is restricted to a single N, M across
        // all links; adjust this when the simulator is ready
        ParamOption optChSp = getChannelSpacing();
        ParamOption optChNr = getChannelNumber();

        this.mc.getLinkChannel().forEach(i -> {
            // default port 0 on the transponder
            switch (i.getSourcePort()) {
            case 0:
                nodeIDs.put(i.getSourceNode().getUri(), new ImmutableMap.Builder<Parameters, ParamOption>()
                        .put(Parameters.ChSp_0, optChSp).put(Parameters.ChNr_0, optChNr).build());
                break;
            case 1:
                nodeIDs.put(i.getSourceNode().getUri(), new ImmutableMap.Builder<Parameters, ParamOption>()
                        .put(Parameters.ChSp_1, optChSp).put(Parameters.ChNr_1, optChNr).build());
                break;
            }
            switch (i.getDestinationPort()) {
            case 0:
                nodeIDs.put(i.getDestinationNode().getUri(), new ImmutableMap.Builder<Parameters, ParamOption>()
                        .put(Parameters.ChSp_0, optChSp).put(Parameters.ChNr_0, optChNr).build());
                break;
            case 1:
                nodeIDs.put(i.getDestinationNode().getUri(), new ImmutableMap.Builder<Parameters, ParamOption>()
                        .put(Parameters.ChSp_1, optChSp).put(Parameters.ChNr_1, optChNr).build());
                break;
            }

        });
        return nodeIDs;
    }

    /**
     * 
     * @return the media channel
     */
    public MediaChannelListType getMC() {
        return this.mc;
    }

    /**
     * 
     * @return the channel spacing option
     */
    public ParamOption getChannelSpacing() {
        switch (this.mc.getEffectiveFreqSlot().getM()) {
        case 1:
            return ChSp._12_5GHZ;
        case 2:
            return ChSp._25GHZ;
        case 4:
            return ChSp._50GHZ;
        default:
            return ChSp._25GHZ;
        }
    }

    /**
     * 
     * @return the channel number option
     */
    public ParamOption getChannelNumber() {
        return new ChNr(this.mc.getEffectiveFreqSlot().getN());
    }

    /**
     * 
     * @return the string serialization of the media channel
     * @throws JsonProcessingException
     */
    public String serializeMC() throws JsonProcessingException {
        return mapper.writeValueAsString(this.mc);
    }
}
