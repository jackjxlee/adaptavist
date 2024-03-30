import com.onresolve.jira.groovy.user.FormField
import org.apache.commons.lang3.StringUtils
import com.atlassian.jira.issue.priority.Priority

if (underlyingIssue) {
    return // do not set a default value if the issue already exists
}

final String URGENT = "URGENT: "

def summary = getFieldById('summary')

def priority = getFieldById('priority')
def priorityValue = priority.getValue() as Priority

if (priorityValue.name == "High" || priorityValue.name == "Highest") {
    appendUrgent(summary, URGENT)
} else if (priorityValue.name == "Medium" || priorityValue.name == "Low" || priorityValue.name == "Lowest") {
    removeUrgent(summary, URGENT)
}

def appendUrgent(FormField summary, String prefix) {
    def issueSummary = summary.getValue() != null && !StringUtils.isEmpty(summary.getValue().toString()) ? summary.getValue().toString() : prefix
    if (issueSummary.equals(prefix)) {
        summary.setFormValue(issueSummary)
    } else if (issueSummary.startsWith(prefix)) {
        summary.setFormValue(issueSummary)
    } else {
        summary.setFormValue(prefix + issueSummary)
    }
}

def removeUrgent(FormField summary, String prefix) {
    def issueSummary = summary.getValue() != null ? summary.getValue().toString() : ""
    if (issueSummary.startsWith(prefix)) {
        summary.setFormValue(issueSummary.substring(prefix.length()))
    }
}