# # 🏥 Online Doctor Booking Mobile Application

A mobile healthcare solution designed for hospitals to enable easy and accessible digital consultations and appointment booking — especially catering to elderly and disabled patients.

---

## 📱 Overview

This mobile application serves as a bridge between patients and healthcare providers, offering essential features such as:

- Doctor appointment scheduling  
- Real-time notifications and reminders

It is designed with accessibility in mind, particularly for the elderly and patients with disabilities who may face challenges in visiting hospitals in person.

---

## 👥 User Roles

### 🧑‍💼 Patient

- Book appointments with available doctors.
- Manage appointments: update or cancel appointments.
- Receive reminders and real-time notifications for upcoming appointments.

### 🧑‍⚕️ Doctor

- Manage appointment slots and availability.
- Get notified about upcoming consultations and schedule changes.

---

## 🔑 Key Features

- **🧩 Role-Based Access Control**  
  Secure, structured access depending on whether the user is a doctor or a patient.

- **📆 Doctor Appointment Schedule**  
  Doctors can set and update availability; patients can view and book appointments accordingly.

- **🔔 Real-Time Notifications & Reminders**  
  Automated alerts for upcoming appointments, call notifications, and schedule changes.

---

## 🛠️ Technology Stack

### Backend

- **Spring Boot Framework** – for scalable and robust backend services  
- **JWT (JSON Web Token)** – for secure authentication and authorization  
- **RESTful API** – to handle communication between frontend and backend

### Frontend

- **Android Studio** – native Android application development  
- **XML** – for UI design and layout

### Database

- **PostgreSQL** – to manage structured hospital, user, and appointment data

### Other Techniques

- **Custom Booking System** – handles doctor availability and patient scheduling  
- **Real-time Notification Service** – keeps users updated with alerts and reminders

---

## 🚀 Expected Outcomes

By the end of development, the hospital will have:

- A working mobile application for digital healthcare access  
- Structured role-based system for both patients and doctors  
- Real-time consultation and scheduling capabilities  
- Improved access to healthcare for elderly and disabled patients  

---

## 🐞 Known Issues & Bug Reporting

We are constantly working to improve the platform. Known limitations and ongoing improvements include:

### 🔍 Known Issues

- **Double Booking Issue**  
  There are scenarios where multiple patients can book the same time slot with the same doctor. This occurs due to race conditions and lack of concurrency control. A locking mechanism is in progress to resolve this.

- **Duplicate User Entry**  
  Occasionally, the same user (especially patients) is recorded multiple times in the system due to repeated registration or failed request handling. This results in login issues and data inconsistencies. Future updates will implement better validation and deduplication logic.

---

## 🔮 Future Enhancements

To build upon current achievements and address limitations, future development will focus on the following areas:

### 1. **Enhanced Online Consultation Features**
- Improve audio/video/chat communication between patients and doctors.
- Expand the notification system to support **incoming call/message alerts**.

### 2. **User Interface and Accessibility Improvements**
- Redesign dashboard, profile, and booking screens based on **patient feedback**.
- Improve UI experience for elderly and low-tech users.

### 3. **Cross-Device and Screen Compatibility**
- Perform **cross-platform testing** for a wide range of devices and Android versions.
- Ensure smooth and consistent experience on different screen sizes.

### 4. **Doctor Rating and Review System**
- Allow patients to **rate and review** doctors post-consultation.
- Improve transparency and service quality.

### 5. **Checkout and Payment Integration**
- Introduce a **payment confirmation system** for online consultations.
- Provide clear, secure steps to confirm and complete consultation fees.

### 6. **AI-Powered Features (Long-Term Vision)**
- Implement an **AI-based symptom checker** to assist patients before consultations.
- Use AI to enhance diagnostic support and decision-making for doctors.

---