

Small Android Application using the Toulouse Metropole Open Data APIs. Coded in an afternoon at school, 
I might decide to come back to it later. In the meantinme feel free to fork and make pull requests if you
want to.

** USAGE **

Allows the user to see his position on a map and to preset a radius. The map will then show every recycling point for used 
glass (Beer bottles, jelly jar....) in this radius. Clicking on map pins will give more information about coordinates of the 
collecting point.


** TECHNICAL **

- Robospice Library for all GET requests to Toulouse Metropole's APIs.
- MapView with GoogleMap APIs to display points to user.
- SeekBar to select search radius.
- Used http://www.jsonschema2pojo.org/ to create all classes relative to APIs response.

** LANGUAGES **

Only supports French since it's specific to a French city but feel free to fork and translate for fun.
