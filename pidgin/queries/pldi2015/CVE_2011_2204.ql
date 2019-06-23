let sources = pdg.fieldsNamed("AbstractUser.password") in
let sinks = pdg.formalsOf("Exception") in
pdg.between(sources,sinks) is empty
