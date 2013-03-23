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
          type: "capability.motionSensor",
          description: "Tap to set",
          multiple: false
        ]
      ]
    ],
    [
      title: "And person 1 isn't home...",
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
      title: "And person 2 isn't home...",
      input: [
        [
          name: "p2",
          title: "Which sensor?",
          type: "capability.presenceSensor",
          description: "Tap to set",
          multiple: false
        ]
      ]
    ]
  ]
]}


def installed() {
  log.trace "Installed with settings: ${settings}"
  subscribe(accel.accelerationSensorleration)
  subscribe(p1.presence)
  subscribe(p2.presence)
}

def updated() {
  log.trace "Updated with settings: ${settings}"
  unsubscribe()
  subscribe(motion.motion)
  subscribe(p1.presence)
  subscribe(p2.presence)
}


def shouldNotify(sensor) {
  log.trace "should notify? ... "
  def recentEvents = sensor.eventsSince(new Date(now() - 1000))
  def alreadySent = recentEvents.count { it.value && it.value == "active" } > 1
  !alreadySent
}


def present(sensor){ sensor.latestValue == "present" }
def notPresent(sensor) { sensor.latestValue == "not present" }

def allNotPresent(sensor1, sensor2){
  // I know there is probably a way more terse way terseo do this ... but alas... 
  // my groovy skills are not quite there... 
  notPresent(sensor1) && notPresent(sensor2)
}

def motion(evt) {
  log.debug "${motion.name} evt.value: ${evt.value}"

  if (evt.value == 'active'){
    log.info("Motion detected")
    if (shouldNotify(motion) && allNotPresent(p1, p2)) {
      log.info " *** ${p1.label} && ${p2.label} not pesent! *** "
      notifyMe "Motion detected by ${accel.label ?: accel.name} and nobody is home! ${new Date(now())}" 
    //} else {
      //log.info "Don't notify ..."
    }
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
}

