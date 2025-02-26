# Prograss Sheet

### Why use Progress Shit?
This is a material and improved version that can be a good alternative to the Dialog Progress and it is also much easier to use.

### How to use? 

Currently this library is not published in Mevan, you can add the module

using in java

```java
   PrograssSheet sheet = new PrograssSheet(this);

    binding.btn.setOnClickListener(
        i -> {
          sheet.setMode(StateMod.PROGRASSH);
          sheet.setTitle("Hello");
          sheet.show();
        });
    binding.btn.setOnLongClickListener(
        i -> {
          sheet.setMode(StateMod.PROGRASSV);
          sheet.setTitle("Hello ");
          sheet.show();
          return true;
        });
```

```kt
    val sheet = PrograssSheet(this)
    
    binding.btn.setOnClickListener {
        sheet.setMode(StateMod.PROGRASSH)
        sheet.setTitle("Hello ")
        sheet.show()
    }
    
    binding.btn.setOnLongClickListener {
        sheet.setMode(StateMod.PROGRASSV)
        sheet.setTitle("Hello ")
        sheet.show()
        true
    }
```

### Mod
You can define an item with the help of State Mod in 2 modes: V&H (vertical and horizontal).

