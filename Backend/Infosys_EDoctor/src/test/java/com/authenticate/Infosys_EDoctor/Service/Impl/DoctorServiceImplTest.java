package com.authenticate.Infosys_EDoctor.Service.Impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.authenticate.Infosys_EDoctor.Entity.Doctor;
import com.authenticate.Infosys_EDoctor.Repository.DoctorRepository;
import com.authenticate.Infosys_EDoctor.Service.AppointmentService;
import com.authenticate.Infosys_EDoctor.Service.DoctorAvailabilityService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {DoctorServiceImpl.class, PasswordEncoder.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class DoctorServiceImplTest {
    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private DoctorAvailabilityService doctorAvailabilityService;

    @MockBean
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorServiceImpl doctorServiceImpl;

    @MockBean
    private EmailService emailService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    /**
     * Test {@link DoctorServiceImpl#addDoctor(Doctor)}.
     * <ul>
     *   <li>Then {@link Doctor} (default constructor) Password is
     * {@code secret}.</li>
     * </ul>
     * <p>
     * Method under test: {@link DoctorServiceImpl#addDoctor(Doctor)}
     */
    @Test
    @DisplayName("Test addDoctor(Doctor); then Doctor (default constructor) Password is 'secret'")
    void testAddDoctor_thenDoctorPasswordIsSecret() {
        // Arrange
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");

        Doctor doctor = new Doctor();
        doctor.setChargedPerVisit(1);
        doctor.setDoctorId("42");
        doctor.setEmail("jane.doe@example.org");
        doctor.setHospitalName("Hospital Name");
        doctor.setLocation("Location");
        doctor.setMobileNo("Mobile No");
        doctor.setName("Name");
        doctor.setPassword("iloveyou");
        doctor.setSpecialization("Specialization");
        when(doctorRepository.save(Mockito.<Doctor>any())).thenReturn(doctor);
        doNothing().when(emailService).sendDoctorIdEmail(Mockito.<String>any(), Mockito.<String>any());

        Doctor doctor2 = new Doctor();
        doctor2.setChargedPerVisit(1);
        doctor2.setDoctorId("42");
        doctor2.setEmail("jane.doe@example.org");
        doctor2.setHospitalName("Hospital Name");
        doctor2.setLocation("Location");
        doctor2.setMobileNo("Mobile No");
        doctor2.setName("Name");
        doctor2.setPassword("iloveyou");
        doctor2.setSpecialization("Specialization");

        // Act
        Doctor actualAddDoctorResult = doctorServiceImpl.addDoctor(doctor2);

        // Assert
        verify(emailService).sendDoctorIdEmail(eq("jane.doe@example.org"), eq("42"));
        verify(doctorRepository).save(isA(Doctor.class));
        verify(passwordEncoder).encode(isA(CharSequence.class));
        assertEquals("secret", doctor2.getPassword());
        assertSame(doctor, actualAddDoctorResult);
    }

    /**
     * Test {@link DoctorServiceImpl#addDoctor(Doctor)}.
     * <ul>
     *   <li>Then throw {@link RuntimeException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link DoctorServiceImpl#addDoctor(Doctor)}
     */
    @Test
    @DisplayName("Test addDoctor(Doctor); then throw RuntimeException")
    void testAddDoctor_thenThrowRuntimeException() {
        // Arrange
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");

        Doctor doctor = new Doctor();
        doctor.setChargedPerVisit(1);
        doctor.setDoctorId("42");
        doctor.setEmail("jane.doe@example.org");
        doctor.setHospitalName("Hospital Name");
        doctor.setLocation("Location");
        doctor.setMobileNo("Mobile No");
        doctor.setName("Name");
        doctor.setPassword("iloveyou");
        doctor.setSpecialization("Specialization");
        when(doctorRepository.save(Mockito.<Doctor>any())).thenReturn(doctor);
        doThrow(new RuntimeException("foo")).when(emailService)
                .sendDoctorIdEmail(Mockito.<String>any(), Mockito.<String>any());

        Doctor doctor2 = new Doctor();
        doctor2.setChargedPerVisit(1);
        doctor2.setDoctorId("42");
        doctor2.setEmail("jane.doe@example.org");
        doctor2.setHospitalName("Hospital Name");
        doctor2.setLocation("Location");
        doctor2.setMobileNo("Mobile No");
        doctor2.setName("Name");
        doctor2.setPassword("iloveyou");
        doctor2.setSpecialization("Specialization");

        // Act and Assert
        assertThrows(RuntimeException.class, () -> doctorServiceImpl.addDoctor(doctor2));
        verify(emailService).sendDoctorIdEmail(eq("jane.doe@example.org"), eq("42"));
        verify(doctorRepository).save(isA(Doctor.class));
        verify(passwordEncoder).encode(isA(CharSequence.class));
    }

    /**
     * Test {@link DoctorServiceImpl#getDoctorById(String)}.
     * <p>
     * Method under test: {@link DoctorServiceImpl#getDoctorById(String)}
     */
    @Test
    @DisplayName("Test getDoctorById(String)")
    @Disabled("TODO: Complete this test")
    void testGetDoctorById() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: ApplicationContext failure threshold (1) exceeded: skipping repeated attempt to load context for [MergedContextConfiguration@3d81dd24 testClass = com.authenticate.Infosys_EDoctor.Service.Impl.DiffblueFakeClass233, locations = [], classes = [com.authenticate.Infosys_EDoctor.Service.Impl.DoctorServiceImpl, org.springframework.security.crypto.password.PasswordEncoder], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = [], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@5ff9a588, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@12958874, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@7022bc90, org.springframework.boot.test.web.reactor.netty.DisableReactorResourceFactoryGlobalResourcesContextCustomizerFactory$DisableReactorResourceFactoryGlobalResourcesContextCustomizerCustomizer@13479dc, org.springframework.boot.test.autoconfigure.OnFailureConditionReportContextCustomizerFactory$OnFailureConditionReportContextCustomizer@839e69a, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@70c8d67], contextLoader = org.springframework.test.context.support.DelegatingSmartContextLoader, parent = null]
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:145)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:130)
        //       at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625)
        //       at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        //       at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
        //       at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682)
        //   See https://diff.blue/R026 to resolve this issue.

        // Arrange and Act
        doctorServiceImpl.getDoctorById("42");
    }

    /**
     * Test {@link DoctorServiceImpl#updateDoctor(String, Doctor)}.
     * <p>
     * Method under test: {@link DoctorServiceImpl#updateDoctor(String, Doctor)}
     */
    @Test
    @DisplayName("Test updateDoctor(String, Doctor)")
    @Disabled("TODO: Complete this test")
    void testUpdateDoctor() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: ApplicationContext failure threshold (1) exceeded: skipping repeated attempt to load context for [MergedContextConfiguration@f49abda testClass = com.authenticate.Infosys_EDoctor.Service.Impl.DiffblueFakeClass234, locations = [], classes = [com.authenticate.Infosys_EDoctor.Service.Impl.DoctorServiceImpl, org.springframework.security.crypto.password.PasswordEncoder], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = [], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@5ff9a588, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@12958874, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@7022bc90, org.springframework.boot.test.web.reactor.netty.DisableReactorResourceFactoryGlobalResourcesContextCustomizerFactory$DisableReactorResourceFactoryGlobalResourcesContextCustomizerCustomizer@13479dc, org.springframework.boot.test.autoconfigure.OnFailureConditionReportContextCustomizerFactory$OnFailureConditionReportContextCustomizer@839e69a, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@70c8d67], contextLoader = org.springframework.test.context.support.DelegatingSmartContextLoader, parent = null]
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:145)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:130)
        //       at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625)
        //       at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        //       at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
        //       at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682)
        //   See https://diff.blue/R026 to resolve this issue.

        // Arrange
        Doctor updatedDoctor = new Doctor();
        updatedDoctor.setChargedPerVisit(1);
        updatedDoctor.setDoctorId("42");
        updatedDoctor.setEmail("jane.doe@example.org");
        updatedDoctor.setHospitalName("Hospital Name");
        updatedDoctor.setLocation("Location");
        updatedDoctor.setMobileNo("Mobile No");
        updatedDoctor.setName("Name");
        updatedDoctor.setPassword("iloveyou");
        updatedDoctor.setSpecialization("Specialization");

        // Act
        doctorServiceImpl.updateDoctor("42", updatedDoctor);
    }

    /**
     * Test {@link DoctorServiceImpl#deleteDoctor(String)}.
     * <p>
     * Method under test: {@link DoctorServiceImpl#deleteDoctor(String)}
     */
    @Test
    @DisplayName("Test deleteDoctor(String)")
    @Disabled("TODO: Complete this test")
    void testDeleteDoctor() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: ApplicationContext failure threshold (1) exceeded: skipping repeated attempt to load context for [MergedContextConfiguration@9830760 testClass = com.authenticate.Infosys_EDoctor.Service.Impl.DiffblueFakeClass228, locations = [], classes = [com.authenticate.Infosys_EDoctor.Service.Impl.DoctorServiceImpl, org.springframework.security.crypto.password.PasswordEncoder], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = [], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@5ff9a588, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@12958874, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@7022bc90, org.springframework.boot.test.web.reactor.netty.DisableReactorResourceFactoryGlobalResourcesContextCustomizerFactory$DisableReactorResourceFactoryGlobalResourcesContextCustomizerCustomizer@13479dc, org.springframework.boot.test.autoconfigure.OnFailureConditionReportContextCustomizerFactory$OnFailureConditionReportContextCustomizer@839e69a, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@70c8d67], contextLoader = org.springframework.test.context.support.DelegatingSmartContextLoader, parent = null]
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:145)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:130)
        //       at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625)
        //       at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        //       at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
        //       at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682)
        //   See https://diff.blue/R026 to resolve this issue.

        // Arrange and Act
        doctorServiceImpl.deleteDoctor("42");
    }

    /**
     * Test {@link DoctorServiceImpl#getAvailableSlots(String)}.
     * <p>
     * Method under test: {@link DoctorServiceImpl#getAvailableSlots(String)}
     */
    @Test
    @DisplayName("Test getAvailableSlots(String)")
    @Disabled("TODO: Complete this test")
    void testGetAvailableSlots() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: ApplicationContext failure threshold (1) exceeded: skipping repeated attempt to load context for [MergedContextConfiguration@597df438 testClass = com.authenticate.Infosys_EDoctor.Service.Impl.DiffblueFakeClass232, locations = [], classes = [com.authenticate.Infosys_EDoctor.Service.Impl.DoctorServiceImpl, org.springframework.security.crypto.password.PasswordEncoder], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = [], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@5ff9a588, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@12958874, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@7022bc90, org.springframework.boot.test.web.reactor.netty.DisableReactorResourceFactoryGlobalResourcesContextCustomizerFactory$DisableReactorResourceFactoryGlobalResourcesContextCustomizerCustomizer@13479dc, org.springframework.boot.test.autoconfigure.OnFailureConditionReportContextCustomizerFactory$OnFailureConditionReportContextCustomizer@839e69a, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@70c8d67], contextLoader = org.springframework.test.context.support.DelegatingSmartContextLoader, parent = null]
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:145)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:130)
        //       at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625)
        //       at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        //       at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
        //       at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682)
        //   See https://diff.blue/R026 to resolve this issue.

        // Arrange and Act
        doctorServiceImpl.getAvailableSlots("42");
    }

    /**
     * Test {@link DoctorServiceImpl#findDoctorsBySpecialization(String)}.
     * <p>
     * Method under test:
     * {@link DoctorServiceImpl#findDoctorsBySpecialization(String)}
     */
    @Test
    @DisplayName("Test findDoctorsBySpecialization(String)")
    @Disabled("TODO: Complete this test")
    void testFindDoctorsBySpecialization() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: ApplicationContext failure threshold (1) exceeded: skipping repeated attempt to load context for [MergedContextConfiguration@a8a0d33 testClass = com.authenticate.Infosys_EDoctor.Service.Impl.DiffblueFakeClass230, locations = [], classes = [com.authenticate.Infosys_EDoctor.Service.Impl.DoctorServiceImpl, org.springframework.security.crypto.password.PasswordEncoder], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = [], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@5ff9a588, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@12958874, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@7022bc90, org.springframework.boot.test.web.reactor.netty.DisableReactorResourceFactoryGlobalResourcesContextCustomizerFactory$DisableReactorResourceFactoryGlobalResourcesContextCustomizerCustomizer@13479dc, org.springframework.boot.test.autoconfigure.OnFailureConditionReportContextCustomizerFactory$OnFailureConditionReportContextCustomizer@839e69a, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@70c8d67], contextLoader = org.springframework.test.context.support.DelegatingSmartContextLoader, parent = null]
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:145)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:130)
        //       at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625)
        //       at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        //       at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
        //       at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682)
        //   See https://diff.blue/R026 to resolve this issue.

        // Arrange and Act
        doctorServiceImpl.findDoctorsBySpecialization("Specialization");
    }

    /**
     * Test {@link DoctorServiceImpl#findAllDoctors()}.
     * <p>
     * Method under test: {@link DoctorServiceImpl#findAllDoctors()}
     */
    @Test
    @DisplayName("Test findAllDoctors()")
    @Disabled("TODO: Complete this test")
    void testFindAllDoctors() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: ApplicationContext failure threshold (1) exceeded: skipping repeated attempt to load context for [MergedContextConfiguration@2efbdb37 testClass = com.authenticate.Infosys_EDoctor.Service.Impl.DiffblueFakeClass229, locations = [], classes = [com.authenticate.Infosys_EDoctor.Service.Impl.DoctorServiceImpl, org.springframework.security.crypto.password.PasswordEncoder], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = [], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@5ff9a588, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@12958874, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@7022bc90, org.springframework.boot.test.web.reactor.netty.DisableReactorResourceFactoryGlobalResourcesContextCustomizerFactory$DisableReactorResourceFactoryGlobalResourcesContextCustomizerCustomizer@13479dc, org.springframework.boot.test.autoconfigure.OnFailureConditionReportContextCustomizerFactory$OnFailureConditionReportContextCustomizer@839e69a, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@70c8d67], contextLoader = org.springframework.test.context.support.DelegatingSmartContextLoader, parent = null]
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:145)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:130)
        //       at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625)
        //       at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        //       at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
        //       at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682)
        //   See https://diff.blue/R026 to resolve this issue.

        // Arrange and Act
        doctorServiceImpl.findAllDoctors();
    }

    /**
     * Test {@link DoctorServiceImpl#getAllAppointments(String)}.
     * <p>
     * Method under test: {@link DoctorServiceImpl#getAllAppointments(String)}
     */
    @Test
    @DisplayName("Test getAllAppointments(String)")
    @Disabled("TODO: Complete this test")
    void testGetAllAppointments() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: ApplicationContext failure threshold (1) exceeded: skipping repeated attempt to load context for [MergedContextConfiguration@134ed1ba testClass = com.authenticate.Infosys_EDoctor.Service.Impl.DiffblueFakeClass231, locations = [], classes = [com.authenticate.Infosys_EDoctor.Service.Impl.DoctorServiceImpl, org.springframework.security.crypto.password.PasswordEncoder], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = [], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@5ff9a588, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@12958874, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@7022bc90, org.springframework.boot.test.web.reactor.netty.DisableReactorResourceFactoryGlobalResourcesContextCustomizerFactory$DisableReactorResourceFactoryGlobalResourcesContextCustomizerCustomizer@13479dc, org.springframework.boot.test.autoconfigure.OnFailureConditionReportContextCustomizerFactory$OnFailureConditionReportContextCustomizer@839e69a, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@70c8d67], contextLoader = org.springframework.test.context.support.DelegatingSmartContextLoader, parent = null]
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:145)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:130)
        //       at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625)
        //       at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        //       at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
        //       at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682)
        //   See https://diff.blue/R026 to resolve this issue.

        // Arrange and Act
        doctorServiceImpl.getAllAppointments("42");
    }

    /**
     * Test {@link DoctorServiceImpl#confirmAppointment(Long)}.
     * <p>
     * Method under test: {@link DoctorServiceImpl#confirmAppointment(Long)}
     */
    @Test
    @DisplayName("Test confirmAppointment(Long)")
    @Disabled("TODO: Complete this test")
    void testConfirmAppointment() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: ApplicationContext failure threshold (1) exceeded: skipping repeated attempt to load context for [MergedContextConfiguration@155cc2be testClass = com.authenticate.Infosys_EDoctor.Service.Impl.DiffblueFakeClass227, locations = [], classes = [com.authenticate.Infosys_EDoctor.Service.Impl.DoctorServiceImpl, org.springframework.security.crypto.password.PasswordEncoder], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = [], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@5ff9a588, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@12958874, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@7022bc90, org.springframework.boot.test.web.reactor.netty.DisableReactorResourceFactoryGlobalResourcesContextCustomizerFactory$DisableReactorResourceFactoryGlobalResourcesContextCustomizerCustomizer@13479dc, org.springframework.boot.test.autoconfigure.OnFailureConditionReportContextCustomizerFactory$OnFailureConditionReportContextCustomizer@839e69a, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@70c8d67], contextLoader = org.springframework.test.context.support.DelegatingSmartContextLoader, parent = null]
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:145)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:130)
        //       at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625)
        //       at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        //       at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
        //       at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682)
        //   See https://diff.blue/R026 to resolve this issue.

        // Arrange and Act
        doctorServiceImpl.confirmAppointment(1L);
    }

    /**
     * Test {@link DoctorServiceImpl#cancelAppointment(Long, String)}.
     * <p>
     * Method under test: {@link DoctorServiceImpl#cancelAppointment(Long, String)}
     */
    @Test
    @DisplayName("Test cancelAppointment(Long, String)")
    @Disabled("TODO: Complete this test")
    void testCancelAppointment() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: ApplicationContext failure threshold (1) exceeded: skipping repeated attempt to load context for [MergedContextConfiguration@618f9540 testClass = com.authenticate.Infosys_EDoctor.Service.Impl.DiffblueFakeClass226, locations = [], classes = [com.authenticate.Infosys_EDoctor.Service.Impl.DoctorServiceImpl, org.springframework.security.crypto.password.PasswordEncoder], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = [], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@5ff9a588, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@12958874, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@7022bc90, org.springframework.boot.test.web.reactor.netty.DisableReactorResourceFactoryGlobalResourcesContextCustomizerFactory$DisableReactorResourceFactoryGlobalResourcesContextCustomizerCustomizer@13479dc, org.springframework.boot.test.autoconfigure.OnFailureConditionReportContextCustomizerFactory$OnFailureConditionReportContextCustomizer@839e69a, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@70c8d67], contextLoader = org.springframework.test.context.support.DelegatingSmartContextLoader, parent = null]
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:145)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:130)
        //       at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625)
        //       at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        //       at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
        //       at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682)
        //   See https://diff.blue/R026 to resolve this issue.

        // Arrange and Act
        doctorServiceImpl.cancelAppointment(1L, "Just cause");
    }
}
