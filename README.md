# Exercise-Tracker

In this assignment you will be tasked with creating an application that will record information and
show analysis regarding a user’s walking/running trail. Your application should have the ability to
record the GPS points corresponding to the user location. It should take a GPS point every 5 seconds.
All GPS points should and stored in a GPX file (XML based file format) A sample of which you can find
here: https://en.wikipedia.org/wiki/GPS_Exchange_Format#Sample_GPX_document. When the user
finishes recording the trail, the GPX files should be stored in external storage (accessible to anyone)
and some information should be displayed to the user about the journey they have just made. It is
expected that you display the following information about a user’s journey:
• Average speed
• Total distance
• Time taken
• Minimum and maximum altitude
• A graph of the user’s speed during the trail/journey (Custom control).

These statistics should be shown in a separate activity. The graph should be shown in a custom view.
The custom view can be display only and does not need to be reactive to touch. All activities should
be implemented with standard UI elements that are available with the standard SDK. Do not use any
additional libraries.
In summary, your application will be built around two activities:
1. To start and stop the exercise tracker
2. To report on what was recorded

The layout and design are entirely up to you however you will be required to justify the design and
layout of your UI and CustomView. This will be required in the documentation you will provide.
You will also be tasked with implementing additional feature(s) of your choosing. It will be entirely
your own choice but be aware that the complexity of the feature will be considered when it is being
marked.

The following components should be implemented with the following high-level features:
1. MainActivity:
o Display some suitable controls to
a. Start and stop the tracker
b. Explain how it works
2. ReportActivity:
o Display suitable controls to
a. Report on metrics which have been recorded
b. Graph the metrics which have been recorded
