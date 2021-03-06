/*******************************************************************************
Copyright (c) 2012, S-Core.
All rights reserved.

Use is subject to license terms.

This distribution may include materials developed by third parties.
******************************************************************************/

// FAIL: If input to Array.prototype.concat is array, elements must be concatenated.

var arr1 = [0,1];
var arr2 = [2,3];
var arr3 = arr1.concat(arr2);

var __result1 = arr3[0];
var __expect1 = 0;

var __result2 = arr3[1];
var __expect2 = 1;

var __result3 = arr3[2];
var __expect3 = 2;

var __result4 = arr3[3];
var __expect4 = 3;

var __result5 = arr3[4];
var __expect5 = undefined;
