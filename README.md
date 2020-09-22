# kotlin-exec-lib
Code to run command line system commands from Kotlin.  
```
dependencies {
    implementation("me.szymanski.kotlinexec:kotlin-exec-lib:1.0")
}
```

```
import me.szymanski.kotlinexec.exec

"echo Hello World".exec()
"mkdir newDir".exec()
"git status".exec()
...
```
