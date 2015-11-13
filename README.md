# issues

Figwheel with react-native v0.14.2 and optimizations set to :simple.

All the ideas from [figwheel-react-native](https://github.com/decker405/figwheel-react-native),
me just enumerate experiments, will PR soon.

For now, figwheel works well,  production release build works well.

## Usage

### Run

``` bash
lein figwheel

open ios/issues.xcodeproj
```

### Bundle
1. uncomment this line in ios/issues/AppDelegate.m

``` javascript
jsCodeLocation = [[NSBundle mainBundle] URLForResource:@"main" withExtension:@"jsbundle"];
```
2. react bundle
``` bash
react-native bundle --entry-file index.ios.js --bundle-output ios/main.jsbundle --platform ios --dev false
```
