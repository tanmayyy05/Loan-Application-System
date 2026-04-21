# CrediFlow

This project was generated using [Angular CLI](https://github.com/angular/angular-cli) version 21.0.4.

## Development server

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.

# CrediFlow – Personal Loan Management System (UI)

CrediFlow is a **frontend Angular application** for a Personal Loan Management System.
This project focuses on **clean UI, clear workflows, and backend-ready architecture**.

> ⚠️ Note: This is a **UI-first implementation** using mock data.  
> Backend APIs will be integrated in the next phase.

---

## 🧱 Tech Stack

- Angular (Standalone Components)
- Angular Router
- Reactive Forms
- Tailwind CSS
- TypeScript
- Mock State Services (no backend yet)

---

## 📁 Project Structure (High Level)

# CrediFlow – Personal Loan Management System (UI)

CrediFlow is a **frontend Angular application** for a Personal Loan Management System.
This project focuses on **clean UI, clear workflows, and backend-ready architecture**.

> ⚠️ Note: This is a **UI-first implementation** using mock data.  
> Backend APIs will be integrated in the next phase.

---

## 🧱 Tech Stack

- Angular (Standalone Components)
- Angular Router
- Reactive Forms
- Tailwind CSS
- TypeScript
- Mock State Services (no backend yet)

---

## 📁 Project Structure (High Level)

src/app
│
├── auth/ → Login & Register
│
├── user/ → User module (loan applicant)
│ ├── dashboard/
│ ├── profile/
│ ├── my-loans/
│ ├── emi-schedule/
│ └── apply-loan/
│ ├── steps/
│ │ ├── basic-details/
│ │ ├── personal-details/
│ │ ├── employment-details/
│ │ ├── documents/
│ │ └── review-submit/
│ └── services/
│
├── loan-officer/ → Loan officer module
│ ├── dashboard/
│ ├── applications/
│ ├── application-details/
│ └── history/
│
├── admin/ → Admin module
│ ├── dashboard/
│ ├── applications/
│ ├── disbursements/
│ ├── system-rules/
│ └── audit-logs/
│
├── shared/
│ ├── layouts/
│ │ ├── user-layout/
│ │ ├── loan-officer-layout/
│ │ ├── admin-layout/
│ │ └── apply-loan-layout/
│ └── components/
│ └── application-timeline/
│
├── core/
│ ├── models/
│ ├── utils/
│ └── guards/
│
└── app.routes.ts

---

## 🔐 AUTH MODULE (Demo Only)

Routes:

- `/login`
- `/register`

Features:

- Reactive Forms
- UI-only (no authentication API yet)
- Will be replaced with real auth later

---

## 👤 USER MODULE

Base Route:
/user

### User Layout

- Sidebar navigation
- Header
- `<router-outlet>` for child pages

### User Pages

| Page         | Route                |
| ------------ | -------------------- |
| Dashboard    | `/user/dashboard`    |
| Profile      | `/user/profile`      |
| My Loans     | `/user/my-loans`     |
| Loan Details | `/user/loan/:id`     |
| EMI Schedule | `/user/emi-schedule` |
| Apply Loan   | `/user/apply-loan/*` |

---

## 📝 APPLY LOAN FLOW (MOST IMPORTANT)

### Route-based multi-step flow:

/user/apply-loan/basic
/user/apply-loan/personal
/user/apply-loan/employment
/user/apply-loan/documents
/user/apply-loan/review

### Steps Breakdown

#### 1️⃣ Basic Details

- Loan Amount
- Tenure
- Credit Score
- Eligibility check (credit score ≥ 650)

#### 2️⃣ Personal Details

- Full Name
- DOB
- Gender
- Marital Status
- Address

#### 3️⃣ Employment Details

- Employment Type
- Company Name
- Monthly Income
- Experience

#### 4️⃣ Documents Upload (Mock)

- Aadhaar
- PAN
- Salary Slip
- Upload status shown (mock)

#### 5️⃣ Review & Submit

- Summary of all entered data
- Final submission (mock)

### State Handling

- `ApplyLoanStateService`
- Data stored step-by-step
- Backend-ready structure

---

## 🧾 USER STATUS TIMELINE

Used in:

- User Dashboard
- My Loans
- Loan Officer → Application Details

Backend statuses mapped to UI stages via:
mapStatusToStage()

---

## 👨‍💼 LOAN OFFICER MODULE

Base Route:
/loan-officer

Pages:

- Dashboard
- Applications List
- Application Details
- Decision History

Features:

- Status timeline
- Document preview (mock)
- Approve / Reject actions (mock modal)
- Clean audit trail UI

---

## 🧑‍💼 ADMIN MODULE

Base Route:
/admin

Pages:

- Dashboard (KPIs)
- Applications
- Disbursement Authorization
- System Rules / Config
- Audit Logs

Notes:

- Admin is final authority
- Guards exist but are **temporarily disabled** for development

---

## 🛡️ ROUTE GUARDS (Currently Disabled)

- `AuthGuard`
- `RoleGuard`
- `AdminRoleGuard`

> Guards will be enabled after backend integration.

---
