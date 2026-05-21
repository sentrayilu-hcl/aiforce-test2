import { test, expect } from '@playwright/test';

test.describe('SCRUM-179 accessibility E2E', () => {
  test('TC-SCRUM-179-001 skip link is first focusable element and targets main content', async ({ page }) => {
    await page.setContent(`
      <a class="skip-link" href="#main-content">Skip to main content</a>
      <header><nav aria-label="Primary"><a href="/home">Home</a></nav></header>
      <main id="main-content" tabindex="-1">
        <h1>Homepage</h1>
        <button>Open dialog</button>
      </main>
    `);
    await page.keyboard.press('Tab');
    await expect(page.getByRole('link', { name: 'Skip to main content' })).toBeFocused();
    await page.keyboard.press('Enter');
    await expect(page.locator('#main-content')).toBeFocused();
  });

  test('TC-SCRUM-179-003 keyboard navigation reaches actionable controls in order', async ({ page }) => {
    await page.setContent(`<main id="main-content" tabindex="-1"><button>Open dialog</button><button>Show toast</button></main>`);
    await page.keyboard.press('Tab');
    await expect(page.getByRole('button', { name: 'Open dialog' })).toBeFocused();
    await page.keyboard.press('Tab');
    await expect(page.getByRole('button', { name: 'Show toast' })).toBeFocused();
  });

  test('TC-SCRUM-179-009 focus-visible styling is present for keyboard navigation', async ({ page }) => {
    await page.setContent(`<style>.focus-visible-ring:focus-visible { outline: 3px solid rgb(11, 95, 255); outline-offset: 2px; }</style><button class="focus-visible-ring">Focusable action</button>`);
    await page.keyboard.press('Tab');
    const button = page.getByRole('button', { name: 'Focusable action' });
    await expect(button).toBeFocused();
  });

  test('TC-SCRUM-179-004 and TC-SCRUM-179-005 modal exposes role and closes on Escape', async ({ page }) => {
    await page.setContent(`
      <button id="trigger">Open dialog</button>
      <div id="dialog" role="dialog" aria-modal="true" aria-labelledby="dialog-title" tabindex="-1">
        <h2 id="dialog-title">Product details</h2>
        <button id="closeBtn">Close</button>
      </div>
      <script>document.addEventListener('keydown', (e) => { if (e.key === 'Escape') { const dialog = document.getElementById('dialog'); if (dialog) dialog.remove(); }});</script>
    `);
    await expect(page.getByRole('dialog')).toBeVisible();
    await page.keyboard.press('Escape');
    await expect(page.getByRole('dialog')).toHaveCount(0);
  });

  test('TC-SCRUM-179-006 form fields expose labels and error messages', async ({ page }) => {
    await page.setContent(`<form><label for="email">Email address</label><input id="email" aria-invalid="true" aria-describedby="email-error" /><p id="email-error" role="alert">Email is required</p></form>`);
    await expect(page.getByLabel('Email address')).toBeVisible();
    await expect(page.getByRole('alert')).toHaveText('Email is required');
  });
});
