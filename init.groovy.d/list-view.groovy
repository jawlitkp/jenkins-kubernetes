import hudson.model.View;
import hudson.model.*;
import jenkins.model.*;

private InputStream generateXMLForView(String name, String jobRegex) {
    def xml = """<listView>
	  <name>${name}</name>
      <filterExecutors>false</filterExecutors>
      <filterQueue>false</filterQueue>
      <properties class="hudson.model.View\$PropertyList"/>
      <jobNames>
        <comparator class="hudson.util.CaseInsensitiveComparator"/>
      </jobNames>
      <jobFilters/>
      <columns>
		<hudson.views.BuildButtonColumn/>
        <hudson.views.StatusColumn/>
        <hudson.views.WeatherColumn/>
        <hudson.views.JobColumn/>
        <hudson.views.LastSuccessColumn/>
        <hudson.views.LastFailureColumn/>
        <hudson.views.LastDurationColumn/>
      </columns>
	  <includeRegex>${jobRegex}</includeRegex>
      <recurse>false</recurse>
    </listView>"""
    return new ByteArrayInputStream(xml.toString().getBytes());
}

def createView(String viewName, String jobRegex) {
    Jenkins instance = Jenkins.getInstance()

    if(instance.getView(viewName) == null) {
        println("Created view \"${viewName}\".")
        View newView = View.createViewFromXML(viewName, generateXMLForView(viewName, jobRegex))
        instance.addView(newView)
        instance.save()
    } else {
        println "Nothing changed.  View \"${viewName}\" already exists."
    }
}

createView('exp','.*?(test|exp).*')
createView('inu','.*?([iI][nN][uU]|storedq).*')
createView('infra','.*?(sbt|jenkins|elasticsearch|jnlp|docker|kube).*')
