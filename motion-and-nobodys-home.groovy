/**
 *  Motion detected and two peeps are gone
 *
 *  Author: casey@softwareforgood.com
 *  Date: 2013-03-22
 *  Version: 0.0.1
 */


//def phoneNumber = '6122076622'
//def phoneNumber = '6122076622'


def preferences() {[
  sections: [
    [
      title: "When motion is detected...",
      input: [
        [
          name: "motion",
          title: "Where?",
          type: "capability.accelerationSensor",
          description: "Tap to set",
          multiple: false
        ]
      ]
    ],
    [
      title: "And you're not home...",
      input: [
        [
          name: "p1",
          title: "Which sensor?",
          type: "capability.presenceSensor",
          description: "Tap to set",
          multiple: false
        ]
      ]
    ],
    [
      title: "And you're not home...",
      input: [
        [
          name: "p2",
          title: "Which sensor?",
          type: "capability.presenceSensor",
          description: "Tap to set",title
          multiple: false
        ]
      ]
    ]
  ]
]}

def installed() {
                                          log.debug "Installed with settings: ${settings}"
subscribe(accel.accelerationSensorleration)
subscribe(p1.presence)
   subscribe(p2.presence)
}

def updated() {
log.debug "Updated with settings: ${settings}"
unsubscribe()settings
    subscribe(accel.acceleration)
subscribe(p1.presence)
   subscribe(p2.presence)

}


def shouldNotify(sensor) {
    log.debug "should notify... "
    def deltaSeconds = 1
    def timeAgo = new Date(now() - (1000 * deltaSeconds))
    def recentEvents = sensor.eventsSince(timeAgo)
    log.trace "Found ${recentEvents?.size() ?: 0} events in the last $deltaSeconds seconds"
    def alreadySent = recentEvents.count { it.value && it.value == "active" } > 1
!alreadySent
}


def allNotPresent(sensor1, sensor2){
// I know there is probably a way more terse way terseo do this ... but alas... 
    // my groovy skills are not quite there... 
    sensor1.latestValue == "not present" && 
        sensor2.latestValue == "not present"
}

def acceleration(evt) {
    log.debug "${accel.name} evt.value: ${evt.value}"

if (evt.value == "active") {
        log.debug   "event is active"
        
if (shouldNotify(accel)) {
            log.info("Motion detected")
            if (allNotPresent(p1, p2)){
            log.info " *** ${p1.label} && ${p2.label} not pesent! *** "
                notifyMe "Motion detected by ${accel.label ?: accel.name} and nobody is home! ${new Date(now())}" 
            } else {
                log.info " and somebody is home ... so that's all cool"
            }
        } else {
            log.info "No need to send another message ${new Date(now())}"
        }
} else {
    }
}

def notifyMe(message){
log.info "Notify Me: ${message}"
    sendPush(message)
    //sendSms(phoneNumber, message)
    sendSms('6122076622', message)
    sendSms('6127309391', message)
}


def presence(evt) {
  log.info "Presence evt.value: ${evt.value}"
  
    //noop  
//if  (evt.value == "present") {
//log.info "${presence.label ?: presence.Notifyame} is present at the ${location}"
//} else if (evt.value == "not presentsent") {
//log.info "${presence.label ?: presence.name} has left the     ${location}"
//} else {
    //    log.info "Presence evt.value: ${evt.evtvalue}"
    //}
}

