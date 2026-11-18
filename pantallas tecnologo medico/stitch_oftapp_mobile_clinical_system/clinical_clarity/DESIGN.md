---
name: Clinical Clarity
colors:
  surface: '#f8f9ff'
  surface-dim: '#ccdbf4'
  surface-bright: '#f8f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#eff4ff'
  surface-container: '#e6eeff'
  surface-container-high: '#dde9ff'
  surface-container-highest: '#d5e3fd'
  on-surface: '#0d1c2f'
  on-surface-variant: '#3f484e'
  inverse-surface: '#233144'
  inverse-on-surface: '#ebf1ff'
  outline: '#6f787f'
  outline-variant: '#bec8cf'
  surface-tint: '#006689'
  primary: '#006385'
  on-primary: '#ffffff'
  primary-container: '#067da7'
  on-primary-container: '#fbfcff'
  inverse-primary: '#79d1fe'
  secondary: '#006970'
  on-secondary: '#ffffff'
  secondary-container: '#6ff2fe'
  on-secondary-container: '#006e75'
  tertiary: '#325f9b'
  on-tertiary: '#ffffff'
  tertiary-container: '#4d78b6'
  on-tertiary-container: '#000512'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#c3e8ff'
  primary-fixed-dim: '#79d1fe'
  on-primary-fixed: '#001e2c'
  on-primary-fixed-variant: '#004d68'
  secondary-fixed: '#7bf4ff'
  secondary-fixed-dim: '#50d9e4'
  on-secondary-fixed: '#002022'
  on-secondary-fixed-variant: '#004f54'
  tertiary-fixed: '#d5e3ff'
  tertiary-fixed-dim: '#a7c8ff'
  on-tertiary-fixed: '#001c3b'
  on-tertiary-fixed-variant: '#124782'
  background: '#f8f9ff'
  on-background: '#0d1c2f'
  surface-variant: '#d5e3fd'
typography:
  headline-lg:
    fontFamily: Atkinson Hyperlegible Next
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.01em
  headline-lg-mobile:
    fontFamily: Atkinson Hyperlegible Next
    fontSize: 26px
    fontWeight: '700'
    lineHeight: 34px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Atkinson Hyperlegible Next
    fontSize: 22px
    fontWeight: '700'
    lineHeight: 30px
    letterSpacing: '0'
  headline-sm:
    fontFamily: Atkinson Hyperlegible Next
    fontSize: 19px
    fontWeight: '600'
    lineHeight: 26px
    letterSpacing: '0'
  body-lg:
    fontFamily: Atkinson Hyperlegible Next
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
    letterSpacing: 0.01em
  body-md:
    fontFamily: Atkinson Hyperlegible Next
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
    letterSpacing: 0.01em
  body-sm:
    fontFamily: Atkinson Hyperlegible Next
    fontSize: 14px
    fontWeight: '500'
    lineHeight: 20px
    letterSpacing: 0.02em
  label-lg:
    fontFamily: Atkinson Hyperlegible Next
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 22px
    letterSpacing: 0.02em
  label-md:
    fontFamily: Atkinson Hyperlegible Next
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 18px
    letterSpacing: 0.03em
  label-sm:
    fontFamily: Atkinson Hyperlegible Next
    fontSize: 12px
    fontWeight: '700'
    lineHeight: 16px
    letterSpacing: 0.04em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1rem
  gutter-mobile: 0.75rem
  margin: 1.25rem
  margin-mobile: 1rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style

This design system establishes an authoritative, calming, and deeply accessible clinical experience for Android ophthalmic mobile healthcare. Built upon the principles of Material 3 and specialized low-vision accessibility patterns, the interface balances precision diagnostics with empathetic patient care.

The visual style is **Corporate / Modern** infused with surgical cleanliness and clinical clarity:
- **Atmosphere:** Sterile yet reassuring, prioritizing cognitive ease and optical relief through expansive cool whites, deep nautical anchors, and luminous cyan accents.
- **Accessibility Anchor:** Designed strictly to support users experiencing ocular strain, recovery, or impaired vision. High visual weight, unambiguous color boundaries, strict high-contrast ratios, and large physical interactive surfaces take precedence over decorative flair.
- **Tone:** Methodical, trustworthy, safe, and immediate.

## Colors

The palette is engineered for clinical precision and high optical contrast, strictly maintaining contrast ratios above 4.5:1 for body and supporting text, and above 7:1 for headline copy.

- **Primary (`#0F80AA` - Clinical Teal-Blue):** Drives principal interactive actions, primary buttons, active state indicators, and progress bars.
- **Secondary (`#20BAC5` - Diagnostic Cyan):** Applied to focal highlights, interactive toggles, badge rings, and supportive analytical accents.
- **Tertiary / On-Surface Titles (`#1D4E89` - Deep Navy):** Anchors high-contrast headings, app bar titles, and critical visual hierarchy points against light surfaces.
- **Neutral / Text Body (`#334155` - Slate 700):** Provides optimal legibility for running text, medical notes, and metadata, avoiding the harsh eye fatigue caused by pure `#000000`.
- **Canvas & Backgrounds:** The base canvas uses `#F8FAFC` (Cold Clean Slate 50), with layered surfaces using `#FFFFFF` (Surface Container Lowest) and `#F1F5F9` (Surface Container Low).
- **Clinical Semantic States:**
  - **Pending:** Background `#FEF3C7`, Text `#92400E`, Border `#FDE68A`.
  - **Validated (Success):** Background `#DCFCE7`, Text `#15803D`, Border `#86EFAC`.
  - **Delivered:** Background `#E0F2FE`, Text `#0369A1`, Border `#7DD3FC`.
  - **Observed / Urgent (Error):** Background `#FEE2E2`, Text `#BA1A1A`, Border `#FCA5A5`.

## Typography

The design system standardizes on **Atkinson Hyperlegible Next** across all text hierarchies. Designed intentionally to differentiate easily confused glyphs (such as 'I', 'l', and '1'), this typeface ensures clinical data, patient identifiers, and optical measurement values are unmistakably legible.

- **Scale Rationale:** Base reading sizes start at `16px` (`body-md`) and `18px` (`body-lg`) to facilitate effortless reading for patients with refractive errors or post-dilation vision.
- **Contrast & Legibility:** Titles utilize `#1D4E89` at weights `600` and `700`, while body copy maintains `#334155` at generous line heights (`1.5` to `1.55`) to eliminate crowding.
- **Dynamic Text Support:** All type tokens support native Android dynamic font scaling without component truncation or horizontal overflow.

## Layout & Spacing

The layout is built on a responsive mobile-first 4-column fluid grid tailored for portrait handheld Android usage, scaling gracefully to an 8-column layout on foldable devices and clinical tablets.

- **Touch Margins & Rhythm:** A baseline 8dp rhythm governs all components, with a strict 4dp micro-grid. Screens enforce a persistent outer `margin-mobile` of `1rem` (16dp) to prevent interaction failures near curved screen edges.
- **Ergonomics & Thumb Zone:** Critical interaction targets (examination downloads, share triggers, navigation transitions) are prioritized within the lower two-thirds of the viewport.
- **Bottom Navigation Offset:** A mandatory clearing padding of `80dp` is reserved above the bottom navigation bar to protect content and scrolling actions from occlusion.

## Elevation & Depth

To avoid optical haze and visual disorientation for visually impaired patients, this system eschews heavy drop shadows in favor of **tonal layering combined with crisp, low-contrast bounding borders**.

- **Level 0 (Flat Canvas):** `#F8FAFC`. Base background where inactive information rests.
- **Level 1 (Card Default):** `#FFFFFF` surface bounded by a subtle `1px solid #E2E8F0` border. No drop shadow.
- **Level 2 (Interactive & Hovered Cards):** `#FFFFFF` paired with an ultra-soft ambient shadow (`box-shadow: 0px 4px 12px rgba(15, 128, 170, 0.08)`) and border `#CBD5E1`.
- **Level 3 (Modals, Overlays, and Bottom Sheets):** `#FFFFFF` with ambient diffusion (`box-shadow: 0px 8px 28px rgba(29, 78, 137, 0.14)`) and a `1.5px solid #E2E8F0` edge definition.
- **Level 4 (Sticky Navigation):** Fixed bottom app bar with a subtle top border `1px solid #E2E8F0` and diffuse elevation `0px -2px 10px rgba(0, 0, 0, 0.04)`.

## Shapes

In alignment with Material 3 organic ergonomics:
- **Cards and Examination Containers:** Use `rounded-xl` (16dp) and `rounded-2xl` (24dp) for approachable, comforting contours that clearly delineate discrete clinical tests.
- **Buttons and Inputs:** Use `rounded-lg` (12dp) to `rounded-xl` (16dp), keeping a solid structural container with friendly rounded edges.
- **Chips and Status Badges:** Fully rounded capsule/pill shapes (`rounded-full` / 9999px) to clearly differentiate metadata markers from actionable rectangular cards.

## Components

### 1. Touch Targets & Accessibility Foundation
- Every interactive element maintains an absolute minimum touch boundary of **48dp × 48dp**, regardless of visible asset size.
- Every icon is paired with an explicit textual label positioned adjacent or below it; icons never appear standalone.

### 2. Primary & Secondary Buttons
- **Primary Action (e.g., "Descargar Examen", "Agendar Cita"):**
  - Minimum height of `48dp` (recommended `52dp` for primary CTAs).
  - Background `#0F80AA`, text `#FFFFFF`, font `label-lg`, corner radius `12dp`.
  - Pressed state scales down smoothly to 98% with an overlay of `rgba(255, 255, 255, 0.12)`.
- **Secondary / Outlined Action:**
  - Background `transparent`, border `2px solid #0F80AA`, text `#0F80AA`, font `label-lg`.

### 3. Clinical Examination Cards
- **Structure:** Clean `#FFFFFF` container with `rounded-xl` (16dp) radius, `1px solid #E2E8F0` border, and internal padding of `space-md` (16dp).
- **Hierarchy:** Top row hosts the diagnostic category in `headline-sm` (`#1D4E89`) and the semantic status chip. The body section holds medical practitioner details, date, and eye indicator (OD/OS) in `body-md` (`#334155`). The bottom section exposes explicit, high-contrast actions.

### 4. Status Chips (Clinical Badges)
Pill-shaped badges (`rounded-full`) with a `1px` border and `space-xs` vertical by `space-sm` horizontal padding:
- **Pendiente:** Background `#FEF3C7`, Text `#92400E`, Border `#FDE68A`.
- **Validado:** Background `#DCFCE7`, Text `#15803D`, Border `#86EFAC`.
- **Entregado:** Background `#E0F2FE`, Text `#0369A1`, Border `#7DD3FC`.
- **Observado:** Background `#FEE2E2`, Text `#BA1A1A`, Border `#FCA5A5`.

### 5. Persistent Bottom Navigation Bar
- Fixed at screen bottom, height `68dp`, background `#FFFFFF` with top border `1px solid #E2E8F0`.
- 5 equal destination targets strictly formatted with stacked icon and text:
  1. **Inicio**
  2. **Mis exámenes**
  3. **Historial**
  4. **Compartir**
  5. **Perfil**
- Inactive items: `#334155` (Slate 700) with 24dp icons.
- Active items: `#0F80AA` icon and text label with `font-weight: 700`, underscored by a subtle `#E0F2FE` pill indicator indicator behind the icon.

### 6. Form Inputs & Search
- Height `52dp`, background `#F8FAFC`, border `1.5px solid #CBD5E1`, text `#1D4E89`, placeholder `#64748B`.
- Focus state switches border to `2px solid #0F80AA` with zero outline offset for maximum clarity.

### 7. Eye Indicators (OD / OS Tags)
- Specialized clinical chips for Right Eye (OD - Ojo Derecho) and Left Eye (OS - Ojo Izquierdo).
- Rendered in bold monospaced or high-clarity sans with `#1D4E89` background, `#FFFFFF` text, ensuring unambiguous distinction in test results.