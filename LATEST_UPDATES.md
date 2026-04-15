# AURA Gallery - Recent Updates (April 15, 2026)

## Summary of Changes

### 1. Fixed Currency Display in Cart Total ✓
- **File**: `frontend/aura-gallery-frontend.html`
- **Change**: Updated cart total currency symbol from `$` to `₹` in the `updateCartUI()` function
- **Impact**: Cart totals now consistently display in INR (Indian Rupees) with ₹ symbol
- **Details**: 
  - Line 1579: Changed `document.getElementById('cart-total').textContent='$'+total.toLocaleString();` to use `₹` instead of `$`
  - All price displays now uniformly use ₹ symbol:
    - Gallery artwork prices
    - Cart items
    - Cart total
    - 3D viewer preview
    - Checkout confirmation

### 2. Removed Stock Indication from Frontend ✓
- **File**: `frontend/aura-gallery-frontend.html`
- **Change**: Removed the stock display section `<div style="font-size:0.65rem;color:var(--gold);margin-top:0.5rem;letter-spacing:0.1em">Stock: <strong>${a.stock || 1}</strong> ${(a.stock || 1) === 1 ? 'piece' : 'pieces'}</div>`
- **Impact**: Artworks in the gallery no longer display the available stock count

### 2. Added OTP (One-Time Password) Authentication Feature

#### Backend Changes:

**A. User Model Enhancement** (`backend/src/main/java/com/aura/gallery/model/User.java`)
- Added OTP-related fields:
  - `String otp` - stores the 6-digit OTP
  - `LocalDateTime otpCreatedAt` - tracks when OTP was created
  - `LocalDateTime otpExpiresAt` - stores OTP expiration time
  - `Boolean isOtpVerified` - indicates verification status
- Updated getters/setters for all new fields
- Extended UserBuilder to support OTP fields

**B. OTP Utility Class** (`backend/src/main/java/com/aura/gallery/util/OtpUtils.java`)
- `generateOtp()` - Generates random 6-digit OTP
- `getOtpExpirationTime()` - Returns expiration time (10 minutes from now)
- `isOtpExpired()` - Checks if OTP has expired
- `isValidOtp()` - Validates OTP format

**C. OTP Service** (`backend/src/main/java/com/aura/gallery/service/OtpService.java`)
- `sendOtpEmail()` - Sends OTP to email (logs to console in demo)
- `resendOtpEmail()` - Resend OTP functionality

**D. Updated AuthController** (`backend/src/main/java/com/aura/gallery/controller/AuthController.java`)
- **Legacy Endpoints** (kept for backward compatibility):
  - `/api/auth/signup` - Direct signup
  - `/api/auth/login` - Direct login

- **New OTP-Based Endpoints**:
  - `POST /api/auth/signup-request` - Request OTP for signup
    - Input: `{email}`
    - Output: `{message, email}`
  
  - `POST /api/auth/signup-verify` - Verify OTP and complete signup
    - Input: `{name, email, password, role, otp}`
    - Output: `{token, id, name, email, role}`
  
  - `POST /api/auth/login-request` - Request OTP for login
    - Input: `{email, password}`
    - Output: `{message, email}`
  
  - `POST /api/auth/login-verify` - Verify OTP and complete login
    - Input: `{email, otp}`
    - Output: `{token, id, name, email, role}`

#### Frontend Changes:

**A. Authentication Modal UI Update** (`frontend/aura-gallery-frontend.html`)
- **Two-Stage Login Flow**:
  1. Stage 1: User enters email and password
  2. Stage 2: User enters 6-digit OTP received in email

- **Two-Stage Signup Flow**:
  1. Stage 1: User requests OTP by email
  2. Stage 2: User enters OTP, name, password, and role

**B. New JavaScript Functions**:
- `requestLoginOtp()` - Initiates login OTP request
- `verifyLoginOtp()` - Verifies login OTP
- `goBackToLoginStage1()` - Navigate back in login flow
- `requestSignupOtp()` - Initiates signup OTP request
- `doSignupVerify()` - Verifies signup OTP and completes registration
- `goBackToSignupStage1()` - Navigate back in signup flow

**C. Enhanced UI Components**:
- Added OTP input fields with formatting (center-aligned, monospaced numbers)
- Added informational messages about OTP validity (10 minutes)
- Added "Back" buttons to navigate between stages
- Improved error handling for OTP verification

## How It Works

### Signup Flow with OTP:
1. User clicks "Join AURA" button
2. User enters email in Stage 1
3. System sends OTP to email address (logged to console in demo)
4. User advances to Stage 2 with OTP, name, password, and role
5. User submits form
6. System verifies OTP format and creates account
7. JWT token issued and user logged in

### Login Flow with OTP:
1. User clicks "Sign In" button
2. User enters email and password in Stage 1
3. System verifies credentials and sends OTP (logged to console)
4. User advances to Stage 2 and enters OTP
5. System verifies OTP and expiration
6. JWT token issued and user logged in

## Security Considerations

**Current Implementation (Demo)**:
- OTP stored directly in User entity (suitable for development)
- OTP displayed in console logs (for testing)
- No actual email sending (console logging only)

**Production Recommendations**:
1. **OTP Storage**: Use Redis or in-memory cache with TTL instead of database
2. **Email Service**: Integrate with SendGrid, AWS SES, or JavaMailSender
3. **Rate Limiting**: Implement rate limiting for OTP requests
4. **OTP Format**: Consider alphanumeric OTPs for better security
5. **Expiration**: Current 10 minutes is reasonable; consider 5 minutes for higher security
6. **Retry Limits**: Implement maximum OTP verification attempts
7. **HTTPS**: Ensure all communications are encrypted in production

## Testing the Feature

### Test Signup with OTP:
1. Navigate to frontend (http://127.0.0.1:5500)
2. Click "Join AURA"
3. Enter email and request OTP
4. Check console logs (or email in production) for OTP
5. Enter OTP along with name, password, and role
6. Account created successfully

### Test Login with OTP:
1. Click "Sign In"
2. Enter registered email and password
3. Request OTP
4. Check console for OTP code
5. Enter OTP
6. Login successful

## Files Modified

### Frontend (1 file)
- `frontend/aura-gallery-frontend.html` - Removed stock indication, added OTP flow

### Backend (4 files created/modified)
- `backend/src/main/java/com/aura/gallery/model/User.java` - Added OTP fields
- `backend/src/main/java/com/aura/gallery/util/OtpUtils.java` - NEW: OTP utilities
- `backend/src/main/java/com/aura/gallery/service/OtpService.java` - NEW: OTP service
- `backend/src/main/java/com/aura/gallery/controller/AuthController.java` - Added OTP endpoints

## Backward Compatibility

Both the legacy endpoints (`/api/auth/signup`, `/api/auth/login`) and new OTP endpoints are available. Existing integrations will continue to work with the legacy endpoints while new implementations can use the more secure OTP-based flow.

## Next Steps for Production

1. Implement actual email service for OTP delivery
2. Use Redis for OTP storage with TTL
3. Add rate limiting for OTP requests
4. Implement OTP resend functionality
5. Add analytics/logging for authentication events
6. Consider SMS OTP as alternative verification method
7. Add CAPTCHA for additional security
