package org.mao.balance.fwd.impl;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.mao.balance.fwd.intf.OXP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mao on 8/1/16.
 */
@Component(immediate = true)
public class BalanceFwd {

    private final Logger log = LoggerFactory.getLogger(getClass());



    //TODO ----- OXP Service -----
    private OXP oxpSuperTopologyTopo; //for routing_algorithm's get_paths(param); ryu.openexchange.database.topology_data.Super_Topo(domains={},links={})
    private OXP oxpSuperTopologyLocation; //used for looking up host location; ryu.openexchange.database.host_data.Location(locations={})

    //TODO ----- OXP final variable
    private final OXP ETHERTYPE_ARP =  OXP(0x0806);
    private final OXP ETHERTYPE_IPV4 =  OXP(0x0800);


    @Activate
    private void activate() {
        log.info("start");
    }

    @Deactivate
    private void deactivate(){
        log.info("stop");

    }


    //TODO ----- OXP_Utility_Functions
    private boolean checkModeIsCompressed(OXP domain) {return true;} // util.check_mode_is_compressed()


    // ----- Event Process -----
    private void processSBPPacketIn(){

        OXP event; // TODO
        OXP msgPacketIn = event.msg(); // TODO

        OXP domain = msgPacketIn.getDomain();

        OXP pkt = msgPacketIn.getPacketData();

        if(checkModeIsCompressed(domain)){ // FIXME - changeMode and PacketIn is asynchronous, should we check len(data) <= 0 ?

            //FIXME - compress mode not deal ARP ?

            OXP etherType = pkt.getCompressEthertype();
            OXP ipv4Src = pkt.getCompressIpv4Src();
            OXP ipv4Dst = pkt.getCompressIpv4Src();

            shortestForwarding(domain, msgPacketIn, ETHERTYPE_IPV4, ipv4Src, ipv4Dst);

        } else {

            OXP etherType = pkt.getEtherType();
            if(etherType.equals(ETHERTYPE_ARP)) {

                OXP arpPkt = pkt.getARP();
                arpForwarding(domain, msgPacketIn, arpPkt.getDstIp());

            } else if(etherType.equals(ETHERTYPE_IPV4)) {
                OXP ipv4Pkt = pkt.getIPV4();
                shortestForwarding(domain, msgPacketIn, ETHERTYPE_IPV4, ipv4.getSrcAddress(), ipv4.getDstAddress());
            }
        }


    }


    // ----- Auxiliary functions -----
    private void arpForwarding(OXP domain, OXP msg, OXP arpDstIp){} // FIXME - should arp response point to Gateway?
    private void shortestForwarding(OXP domain, OXP msg, OXP ipSrc, OXP ipDst){}

}
