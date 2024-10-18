# PitDev Build Logic
Contains conventions that we can use in other projects
(Inspired by https://github.com/android/nowinandroid)[https://github.com/android/nowinandroid]

You can check if all tests passed
```
gradlew check
```

coverage report (reports are into [module]/reports/jacoco)
```
gradlew jacocoTestReport
```

aggregate report 
````
gradlew clean jacocoTestReport jacocoAggregatedReport
````
