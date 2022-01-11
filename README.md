# MedInfoSys
## Brief description
MedInfoSys is a data management system that facilitates processing of patient information.
It supports three types of user accounts: ADMIN, DOCTOR, PATIENT. A user account can be granted more than one role.
All input forms have data validation on the client and the server side to prevent crashes caused by invalid data.

The ADMIN can register a new user with roles ADMIN, DOCTOR or PATIENT (at least one role must be selected).
The application generates initial username and password which can be changed later by the user itself or by admin.
The admin has access to control panel providing the ability to view/edit personal information of user accounts, invalidate user's sessions, lock accounts and soft delete accounts.
The admin can also set/change the list of medical specialties of a doctor.
The admin does not have access to the medical information of the patients.

The DOCTOR can register a new user account with role PATIENT. 
The application generates initial username and password which can be changed later by the patient itself or by admin.
The doctor has access to control panel providing the ability to view/edit personal information of patients and register a psychical examination of a patent.
Doctor have access to the medical information of the patients.

The PATIENT has access to his personal profile page. It contains the personal details of the patient and the history of all physical examinations. The patient can change his login credentials.

## Packages info
#### config - contains configuration classes:
* Spring security configuration;
* Custom beans - ModelMapper, PasswordEncoder (required by Spring security), Cloudinary, SessionRegistry.
#### exception - contains custom exception classes:
* ObjectAlreadyExistsException - returning http status 409 conflict to the client;
* ObjectNotFoundException - returning http status 404 not found to the customer.
#### init - contains initial database data;
#### model - contains models:
* entity models- required for O/R mapping:
  * BaseEntity - inherited by all entities. Includes ID field;
  * UserRoleEntity - holds personal information of the users of the system;
  * UserEntity - defines ADMIN, DOCTOR and PATIENT user roles;
  * CountryEntity - holds countries;
  * CloudinaryPictureEntity - hold the publicID and URL of images uploaded to Claudinary;
  * PatientEntity - holds patients data;
  * DoctorEntity - holds doctors data;
  * MedicalSpecialtyEntity - holds medical specialties;
  * PhysicalExaminationEntity - holds physical examinations' data.
* service models, binding models, view models - required to prevent data leak and reduce the data traffic.
#### repository - contains JPA repository interfaces;
#### schedules - contains scheduler for anonymization of the data of soft deleted users;
#### service - contains the business logic:
* UserService - provides following methods:
  * Find user by ID;
  * Find user by username;
  * Find user by personal citizen number;
  * Find all users;
  * Find all enabled users;
  * Save user to DB;
  * Patch user;
  * Invalidate user session;
  * Get count of users with active session;
  * Generate new user credentials;
  * Check if user has DOCTOR role;
  * Check if user has PATIENT role;
  * Mark user for soft delete;
  * Anonymize soft deleted user;
  * Lock user account.
  

* CountryService - provides following methods:
  * Find all countries;
  * Find country by name;
  * Find coutry by ID.


* UserRoleService - provides following methods:
  * Save user role to DB;
  * Find all user roles;
  * Find user role by id.


* PatientService - provides following methods:
  * Create patient;
  * Find patient by user's personal citizen number;
  * Find all patients;
  * Find patient by user ID.


* DoctorService - provides following methods:
  * Create doctor;
  * Find doctor by user's personal citizen number;
  * Find doctor by user ID;
  * Find doctor by username;
  * Patch doctor's mediacal specialties.


* MedicalSpecialtyService - provides following methods:
  * Find medical specialty by ID;
  * Find all medical specialties;
  * Save medical specialty to DB.


* PhysicalExaminationService - provides following methods:
  * Save physical examination to DB;
  * Find all physical examinations by user's ID;
  * Find physical examination by Id.


* UserAccessStatsService - holds onRequest method which logs the requests sent from the client to the server and the server response codes. The onRequest method is called by the preHandle method of UserAccessStatsInterceptor.


* UserDetailsServiceImpl - implements UserDetailsService - core interface which loads user-specific data. Overrides loadUserByUsername method required for proper Spring security configuration.


* PatientAspect - holds advices for creating doctor or patient entity:
  * proceedRoles - called around UserService.saveToDb method - if a newly registered user has doctor/patient role, the appropriate method PatientService.createPatient/DoctorService.createDoctor is called;
  * patchRoles - called around UserService.patchUser method - if an existing user is patched with doctor/patient role, the appropriate method PatientService.createPatient/DoctorService.createDoctor is called.
#### utils - contains custom mapper class;
#### web - contains web controllers:
* AdminController;
* DoctorController;
* UserController;
* HomeController;
* MedInfoSysRestController;
* GlobalExceptionHandler.
