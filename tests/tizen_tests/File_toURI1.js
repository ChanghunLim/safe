/*******************************************************************************
    Copyright (c) 2013, S-Core.
    All rights reserved.

    Use is subject to license terms.

    This distribution may include materials developed by third parties.
 ******************************************************************************/
var __result1;
tizen.filesystem.resolve(
   'music',
   function(dir) {
     __result1 = dir.toURI();
   }, function(e) {

   }, "r"
 );



var __expect1 = "file:///opt/usr/media/music";
