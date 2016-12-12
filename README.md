<h1>Flappy MZ-800 for Android</h1>

Android remake of <a href="https://en.wikipedia.org/wiki/Flappy">an old and famous game Flappy</a> originally written for 8-bit
<a href="https://en.wikipedia.org/wiki/Sharp_MZ">Sharp MZ-800</a> computer (CPU <a href="https://en.wikipedia.org/wiki/Zilog_Z80">Z80</a>).
<br>
This source code is based on a <a href="https://sourceforge.net/projects/flappy/">Java remake of Flappy</a>
originally disassembled and rewritten from Z80 machine code by <a href="http://www.8bit-times.eu/">Petr &#x160;lechta</a>.
<br>

<h2>Installation instructions</h2>
From copyright reasons, this game is not hosted in Google Play Store; to install Flappy in your Android device (version 2.3.3 and above), follow the instructions below:
<ol>
    <li>At your device, enable installing application from unknown sources.<br>
      For instructions, see <a href="http://www.androidcentral.com/allow-app-installs-unknown-sources">here</a>.</li>
    <li>Open this page - <i>https://github.com/pe-pan/flappy</i> - in your Android device</li>
    <li>Click on <a href="https://github.com/pe-pan/flappy/releases/download/v0.3-alpha/application-debug.apk">this link</a></li>
    <li>Confirm the installation</li>
</ol>

<h2>Differences from original</h2>
The code contains the original Flappy game with several improvements to reflect Android platform differences.
<ol>
  <li>Finger gestures simulate keyboard arrows (to move Flappy around)</li>
  <li>Finger tap simulates keyboard space bar (to throw a mushroom)</li>
  <li>Dedicated scene selector to skip scenes (requires password)</li>
  <li>Hi-score screen showing scores shared with other players</li>
</ol>

<h2>Code structure</h2>
The code is split into following modules:
<ul>
  <li><b>core</b> - contains the Java version of Flappy made by Petr &#x160;lechta
    <ul>
      <li>plain Java</li>
    </ul>
  </li>
  <li><b>mobile</b> - activities and other Android features
    <ul>
      <li>depends on <b>core</b></li>
      <li>plain Android</li>
      <li><a href="https://cloud.google.com/endpoints/">Android Endpoints</a> to store/read hi-scores</li>
    </ul>
  </li>
  <li><b>application</b> - root module gathering application activities
    <ul>
      <li>depends on <b>mobile</b></li>
    </ul>
  </li>
  <li><b>test</b> - contains test activities
    <ul>
      <li>depends on <b>mobile</b></li>
      <li>to test multi-player score synchronization</li>
      <li>FMI, see <a href="http://stackoverflow.com/questions/35502891/android-integration-test-of-a-distributed-application-best-practices">Android Integration Test of a Distributed Application (best practices)</a> in StackOverflow</li>
    </ul>
  </li>
  <li><b>backend</b> - players' hi-score storage
    <ul>
      <li><a href="https://cloud.google.com/appengine/">Google App Engine</a> hosts the backend app</li>
      <li><a href="https://cloud.google.com/endpoints/">Android Endpoints</a> to access the storage</li>
      <li>persisted in <a href="https://cloud.google.com/datastore/">Google Datastore</a></li>
      <li><a href="https://github.com/objectify/objectify">Objectify</a> to access Google Datastore</li>
      <li>hosted in <a href="https://console.cloud.google.com/">Google Cloud Platform</a></li>
    </ul>
  </li>
</ul>

<h2>Contact</h2>
For feedback or bug reports, contact <a href="http://panuska.net">Petr Panu&#x161;ka</a>.
<br>
&copy; 2016 <a href="http://panuska.net">Petr Panu&#x161;ka</a>