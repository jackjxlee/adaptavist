import java.time.temporal.ChronoUnit

// Get the date values from both issues
def dateCreated = issue.getCreated()
def dateResolved = issue.resolutionDate
// Transform both values to instants
def createDateInstant = dateCreated?.toInstant()
def resolvedDateInstant = dateResolved?.toInstant()

// Calculate the difference between the lower and the higher date.
createDateInstant && resolvedDateInstant ? ChronoUnit.DAYS.between(createDateInstant, resolvedDateInstant) : null