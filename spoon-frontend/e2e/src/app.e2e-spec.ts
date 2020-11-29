import {AppPage} from './app.po';
import {browser, logging} from 'protractor';

describe('workspace-project App', () => {
  let page: AppPage;

  beforeEach(() => {
    page = new AppPage();
  });

  it('should display title', () => {
    page.navigateTo();
    expect(page.getTitleText()).toEqual('Spoon AST visualiser');
  });

  it('textarea should be writeable', () => {
    page.navigateTo();
    page.writeText('Foo Bar');
    const textarea = page.getTextAreaValue();
    expect(textarea).toEqual('Foo Bar');
  });

  it('mat-tree size should fit with text area', () => {
    page.navigateTo();
    expect(page.countTree()).toEqual(4);
    page.writeText('public class hello {}');
    expect(page.countTree()).toEqual(1);
  });

  // TODO
  // it('mat-tree should fit with text area', () => {
  //   page.navigateTo();
  //   page.writeText('public class hello {}');
  //   console.log(page.getMatTreeValue());
  //   // expect(page.getMatTreeValue()).toEqual(['']);
  // });

  afterEach(async () => {
    // Assert that there are no errors emitted from the browser
    const logs = await browser.manage().logs().get(logging.Type.BROWSER);
    expect(logs).not.toContain(jasmine.objectContaining({
      level: logging.Level.SEVERE,
    } as logging.Entry));
  });
});
